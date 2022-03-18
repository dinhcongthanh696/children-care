package childrencare.app.controller;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.*;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("")
public class ScheduleController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationService_Service reservationService_service;

    @Autowired
    Service_service serviceService;

    @Autowired
    StaffService staffService;

    @Autowired
    SlotService slotService;

    @Autowired
    CustomerService customerService;

    @Autowired
    UserService userService;

    @Autowired
    PayService payService;

    @Autowired
    JavaMailSender mailSender;
    //Reservation Completion - KVA


    @GetMapping("/bookingSchedule")
    public String getAllSchedule(Model model, HttpSession session,
                               @RequestParam(name="reservationId") Integer reservationId){
        List<StaffModel> staffs = staffService.getAllStaff();
        model.addAttribute("staffs", staffs);

        List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
        model.addAttribute("services", services);

        List<ReservationServiceModel> schedules =
                reservationService_service.getAllBookedSchedule(reservationId);
        model.addAttribute("schedules", schedules);

        List<Payment> payments = payService.findAll();
        model.addAttribute("payments", payments);

        model.addAttribute("reservationId", reservationId);

        ReservationModel reservation= reservationService.getReservationByID(reservationId);

        model.addAttribute("reservation", reservation);

        double totalPrice = reservation.getTotalReservationPrice() / 23000;
        model.addAttribute("totalPrice", totalPrice);


        return "apppointment";
    }

    @Transactional
    @GetMapping("/updateReservationInfo")
    public String updateInfo(@RequestParam(name = "reservationId") Integer rid,
                           HttpSession session, Model model,
                           HttpServletRequest request, HttpServletResponse response){
        //Update status
        reservationService.changeStatus(rid);
        // Update quantity
        List<ServiceModel> listService = (List<ServiceModel>) session.getAttribute("list");
        if(listService != null) {
            for (ServiceModel sm : listService) {
                serviceService.updateQuantity(sm.getQuantity(), sm.getServiceId());
            }
        }
        session.setAttribute("list", null);
        //Check if the customer is new or changed
        ReservationModel reservation = reservationService.getReservatonInforByID(rid);
        String customerEmail = reservation.getCustomer().getCustomer_user().getEmail();

        CustomerModel customer = customerService.findCustomerByEmail(customerEmail);

        if(customer == null){
            customerService.addNewCustomer(customerEmail);
        }
        else{
            UserModel userModel = customer.getCustomer_user();
            String fullName = userModel.getFullname();
            String mobile = userModel.getPhone();
            boolean gender = userModel.isGender();

            userService.updateInfo(fullName, mobile, gender, customerEmail);
        }

        Cookie cookie = new Cookie("carts", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);



        //Send confirmation email
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
        String from = mailSenderImpl.getUsername();
        String to = "dominhanh171201@gmail.com";
        String subject = "Email confirmation from Children Care";
        List<ReservationServiceModel> reservations = reservationService_service.getAllBookedSchedule(rid);

        MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
        try {
            mimeMessage.setFrom(from);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            MimeBodyPart contentPart = new MimeBodyPart();
            String content = "<h3> Hello " + customerEmail + " , thanks for using our services. Here your schedule: </h3> <br>" ;
            for(ReservationServiceModel rsm : reservations){
                content += "<p>" +  rsm.toString() + "</p>";
            }
            contentPart.setContent(content, "text/html; charset=utf-8");

            MimeBodyPart referencePart = new MimeBodyPart();
            String reference = "<a href='http://localhost:8080/ChildrenCare/thanks'> Children Care's Page</a>";
            referencePart.setContent(reference,"text/html; charset=utf-8");

            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(contentPart);
            multiPart.addBodyPart(referencePart);

            mimeMessage.setContent(multiPart);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "thank_you";
    }


}
