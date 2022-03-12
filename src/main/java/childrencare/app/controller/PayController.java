package childrencare.app.controller;

import childrencare.app.service.PaypalService;
import childrencare.app.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;


@Controller
public class PayController {
    @Autowired
    ReservationService reservationService;

    @Transactional
    @PostMapping("/payment")
    public String checkOut(Model model, @RequestParam(name="reservationId") Integer rid
                        , @RequestParam(name="payMethod") Integer payMethod
                        , @RequestParam(name="price") Double price){

        if(payMethod == 2){
            return "redirect:/pay?price=" + price + "&reservationId=" + rid;
        }
        else{
            reservationService.changeStatusReservation(3, rid);
        }
        return "redirect:/bookingSchedule?reservationId=" + rid;
    }
}
