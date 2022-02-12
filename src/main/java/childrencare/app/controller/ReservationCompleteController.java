package childrencare.app.controller;

import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.Service_service;
import childrencare.app.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("")
public class ReservationCompleteController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    Service_service serviceService;

    @Autowired
    UserService userService;

    //Reservation Completion - KVA
    @PostMapping("/update/{rid}")
    public void reservationComplete(@PathVariable(value="rid") Integer rid ,HttpSession session) {
        // The reservation's status is changed to submitted
        reservationService.changeStatus(rid);
        // Update quantity
        List<ServiceModel> listService = (List<ServiceModel>) session.getAttribute("list");
        for (ServiceModel sm : listService) {
            serviceService.updateQuantity(sm.getQuantity(), sm.getServiceId());
        }
        // Assign to active staff
    }

    @GetMapping("/getDoctor")
    public String getAllDoctor(Model model, HttpSession session){
        List<UserModel> doctors = userService.findAllDoctor();
        model.addAttribute("doctors", doctors);
        List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
        model.addAttribute("services", services);
        return "apppointment";
    }
}
