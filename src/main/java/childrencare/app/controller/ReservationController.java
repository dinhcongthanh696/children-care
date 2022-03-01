package childrencare.app.controller;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.*;
import childrencare.app.repository.CustomerRepository;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    // Nghia's code


    //Nghia's code
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationService_Service reservationService_service;

    // Thanh's code
    @Autowired
    private ServiceModelService serviceModelService;

    @Autowired
    private SlotService slotService;

    @Autowired
    private Service_service service;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BlogCategoryService blogCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/reser")
    public String getServiceCarts(Model model, HttpSession session) {
        List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
        if (itemList != null) {
            double toalReservationPrice = 0;
            for (ServiceModel sm : itemList) {
                toalReservationPrice += sm.getTotalCost();
            }
            session.setAttribute("total", toalReservationPrice);
            model.addAttribute("size", itemList.size());
        }
        session.setAttribute("list",itemList);
        model.addAttribute("list", itemList);
        return "reservationDetails";
    }

    @GetMapping("/add/{sid}")
    public String addToCart(@PathVariable(value = "sid") int id, Model model, HttpSession session,
                            HttpServletResponse response, HttpServletRequest request,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity) throws IOException {
        ServiceModel serviceById = service.getServiceById(id);
        List<ServiceModel> listReservations = null;
        if (session.getAttribute("list") == null) {
            listReservations = new ArrayList<>();
        } else {
            listReservations = (List<ServiceModel>) session.getAttribute("list");
        }
        boolean check = false;
        for (ServiceModel svm : listReservations) {
            if (svm.getServiceId() == id) {
                serviceById = svm;
                serviceById.setQuantity(serviceById.getQuantity() + quantity);
                check = true;
            }
        }

        if (check == false) {
            serviceById.setQuantity(quantity);
            listReservations.add(serviceById);
        }

        // start thanh's code (add service cookie)
        Cookie cartsCookie = CookieHandler.getCookie("carts", request);

        if (cartsCookie != null) {
            CookieHandler.editCartsCookie(id, request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, serviceById.toCookieValue());
        } else {
            CookieHandler.createNewCookie("carts", request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, serviceById.toCookieValue());
        }
        // end thanh's code (Add service cookie)
        session.setAttribute("list", listReservations);
        model.addAttribute("list", listReservations);
        return "redirect:/reservation/reser";
    }
    @DeleteMapping("/delete/{sid}")
    public String deleteFromCart(@PathVariable(value = "sid") int id, HttpSession session,
                               HttpServletResponse response, HttpServletRequest request) {
        List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
        for (ServiceModel service : services) {
            if (service.getServiceId() == id) {
                services.remove(service);
                break;
            }
        }
        CookieHandler.editCartsCookie(id, request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, "");
        return "redirect:/reservation/reser";

    }

    //nghia's code
    @GetMapping("/contact")
    public String getReservationContact(Model model, HttpSession session) {
        List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
        UserModel userModel = (UserModel) session.getAttribute("user");
        if (itemList != null) {
            double total = (Double) session.getAttribute("total");
            model.addAttribute("orderList", itemList);
            model.addAttribute("total", total);
            model.addAttribute("user", userModel);
            return "reservationContact";
        } else {
            return "redirect:/reservation/reser";
        }
    }
    //nghia's code

    @PostMapping("/saveData")
    @Transactional
    public String saveReservation(HttpSession session,
                                  @RequestParam(name = "ten",required = false) String fullname,
                                  @RequestParam(name = "gender",required = false) boolean gender,
                                  @RequestParam(name = "email",required = false) String email,
                                  @RequestParam(name = "phone",required = false) String phone,
                                  @RequestParam(name = "address",required = false) String address,
                                  @RequestParam(name = "note",required = false) String note) {
        //insert to user table
        UserModel user = (UserModel) session.getAttribute("user");
        UserModel userModel = new UserModel();
        userModel.setUsername(email);
        userModel.setFullname(fullname);
        userModel.setAddress(address);
        userModel.setEmail(email);
        userModel.setGender(gender);
        userModel.setNotes(note);
        userModel.setPhone(phone);
        userModel.setStatus(true);
        userService.save(userModel);
        //insert to customer table
        CustomerModel cus = new CustomerModel();
        customerService.insertToCus(1,email);
        //insert to reservation table
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setTotalReservationPrice((Double) session.getAttribute("total"));
        reservationModel.setStatus(false);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        reservationModel.setDate(dtf.format(now));
        int cid = customerService.lastIDCus();
        cus.setCustomer_id(cid);
        reservationModel.setCustomer(cus);
        // thanh's code
        int rid = reservationService.saveReservation(reservationModel);
        List<ServiceModel> serviceCarts = (List<ServiceModel>) session.getAttribute("list");
        //clear cart from session after click submit button (reservation contact) - nghia's code
     /* List<ServiceModel> listSubmit = (List<ServiceModel>) session.getAttribute("list");
        listSubmit.clear(); */
        return "redirect:/bookingSchedule?reservationId=" + rid ;
        // end thanh's code
    }

    @RequestMapping("/infor")
    public String reservationInfor(Model model,
                                   @RequestParam(name = "rid") Optional<Integer> reserID) {
        if (reserID.isPresent()) {
            ReservationModel reservationModel = reservationService.getReservatonInforByID(reserID.get());
            //Slot slot = slotService.getSlotByReservationID(reserID.get());
            List<ServiceModel> serviceModelList = serviceModelService.getServicesByReservationId(reserID.get());
            List<ReservationServiceModel> reservationServices = reservationService_service.getAllBookedSchedule(reserID.get());
            UserModel userModel = userService.findUserModelByUserReservationId(reserID.get());

            model.addAttribute("reservationByReserId", reservationModel);
            model.addAttribute("reservation_ServiceByReserId", reservationServices);
            model.addAttribute("userByReserId", userModel);
            //model.addAttribute("slotByReserId", slot);
            model.addAttribute("listServicesByReserId", serviceModelList);
            model.addAttribute("listCategoryPost", blogCategoryService.findAll());
            return "reservationInfor";
        } else {
            return "redirect:/";
        }


    }

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Transactional
    @GetMapping("/infor/dele")
    public String deleteServiceBySidAndRid(
            @RequestParam("rid") int rid
            , @RequestParam("sid") int sid
            , @RequestParam("slotid") int slotid
            , @RequestParam("date") String date) {
        reservationService_service.deleteByRidAndSidAndSlotid(rid, sid, slotid, date);
        return "redirect:/reservation/infor?rid=" + rid;
    }


}
