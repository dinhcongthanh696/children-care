package childrencare.app.controller;

import childrencare.app.model.*;
import childrencare.app.service.*;
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
    StaffService staffService;

    @Autowired
    SlotService slotService;

    //Reservation Completion - KVA


    @GetMapping("/getDoctor")
    public String getAllDoctor(Model model, HttpSession session,
                               @RequestParam(name="reservationId", required = false) Integer reservationId){
        List<StaffModel> staffs = staffService.getAllStaff();
        model.addAttribute("staffs", staffs);

        List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
        model.addAttribute("services", services);

        model.addAttribute("reservationId", reservationId);

        return "apppointment";
    }



}
