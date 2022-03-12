package childrencare.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PayController {
    @PostMapping("/payment")
    public String checkOut(Model model, @RequestParam(name="reservationId") Integer rid
                        , @RequestParam(name="payMethod") Integer payMethod
                        , @RequestParam(name="price") Double price){


        model.addAttribute("isCheckout", "");
        return "redirect:/bookingSchedule?reservationId=" + rid;
    }
}
