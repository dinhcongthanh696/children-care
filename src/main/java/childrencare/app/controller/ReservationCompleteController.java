package childrencare.app.controller;

import childrencare.app.model.ServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.Service_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/completion")
public class ReservationCompleteController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    Service_service serviceService;

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
}
