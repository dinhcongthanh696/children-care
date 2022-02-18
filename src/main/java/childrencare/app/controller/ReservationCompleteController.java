package childrencare.app.controller;

import childrencare.app.model.*;
import childrencare.app.service.ReservationService;
import childrencare.app.service.Service_service;
import childrencare.app.service.SlotService;
import childrencare.app.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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

    @Autowired
    SlotService slotService;

    //Reservation Completion - KVA
    @Transactional
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
        List<ReservationServiceModel> serviceSlot = (List<ReservationServiceModel>) session.getAttribute("serviceSlot");
        for(ReservationServiceModel rs : serviceSlot){

        }
    }

    @GetMapping("/getDoctor")
    public String getAllDoctor(Model model, HttpSession session){
        List<UserModel> doctors = userService.findAllDoctor();
        model.addAttribute("doctors", doctors);

        List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
        model.addAttribute("services", services);

//        List<Slot> slots = slotService.;
//        model.addAttribute("slots", slots);
        return "apppointment";
    }

    @GetMapping("/updateSchedule")
    public String updateSchedule(HttpSession session){
        List<ReservationServiceModel> serviceSlot = (List<ReservationServiceModel>) session.getAttribute("serviceSlot");
        for(ReservationServiceModel rs : serviceSlot){
            int rid = rs.getReservation().getReservationId();
            int sid = rs.getService().getServiceId();
            int slotId = rs.getSlot().getId();
            String doctor = rs.getDoctor().getUsername();
            if(doctor == "Auto"){

            }
            reservationService.createSchedule(
                    rid,
                    rs.getService().getServiceId(),
                    rs.getSlot().getId(),
                    rs.getDoctor().getUsername(),
                    rs.getPrice()
            );
        }


        return "apppointment";
    }

}
