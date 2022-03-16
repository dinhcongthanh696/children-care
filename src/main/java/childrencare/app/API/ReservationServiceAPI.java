package childrencare.app.API;

import childrencare.app.model.ReservationServiceModel;
import childrencare.app.service.ReservationService_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.sql.Date;

@RestController
@RequestMapping(path = "/manager")
public class ReservationServiceAPI {

    @Autowired
    private ReservationService_Service reservationService_service;

    @Transactional
    @PostMapping("/api-manager/assignOtherStaff")
    public String assignOtherStaff(
            @RequestParam(name = "rid") int rid,
            @RequestParam(name = "staffID") int staffID,
            @RequestParam(name = "bookedDate") Date bookedDate,
            @RequestParam(name = "slotId") int slotId) {
        ReservationServiceModel modelCheck = reservationService_service.checkStaffEmptyDateStaff(bookedDate, slotId, staffID);
        if (modelCheck != null) {
            return "This doctor's time slot is not available";
        }
        if(modelCheck == null){
            reservationService_service.assginOtherStaff(staffID, bookedDate, slotId);
            return "Assign other staff successfully";
        }
        return "";
    }
}
