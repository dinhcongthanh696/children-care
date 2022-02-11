package childrencare.app.controller;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.LoginService;
import childrencare.app.service.ReservationService_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/setting")
public class UserSettingController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ReservationService_Service reservationService_service;

    @GetMapping("/profile")
    public String profileSetting(HttpSession session, Model model){
        UserModel user = (UserModel) session.getAttribute("user");
        model.addAttribute("userLogin",user);
        return "settings";
    }
    @GetMapping("/change")
    public String change(HttpSession session, Model model){
        UserModel user = (UserModel) session.getAttribute("user");
        model.addAttribute("userLogin",user);
        return "changePass";
    }
    @PostMapping("/changePass")
    @Transactional
    public String changePass(HttpSession session, Model model,
                             @RequestParam(name = "pass")String pass,
                             @RequestParam(name = "newpass")String newpass,
                             @RequestParam(name = "renewpass")String renewpass){
        UserModel user = (UserModel) session.getAttribute("user");
        if(!pass.equals(user.getPassword())){
            model.addAttribute("mess", "Pass current invalid!");
        }else {
            if(!newpass.equals(renewpass)){
                model.addAttribute("mess", "Re-pass not equal!");
            }else {
                loginService.changePass(newpass,user.getUsername());
                model.addAttribute("mess", "Change password successfull!");
            }
        }
        return "redirect:/setting/profile";
    }
    @GetMapping("/myReservation")
    public String getmyReservation(Model model,HttpSession session){
        UserModel user = (UserModel) session.getAttribute("user");
        List<ReservationServiceModel> customerReservation = reservationService_service.getCustomerReservation(user.getEmail());
        model.addAttribute("userLogin",user);
        model.addAttribute("customerReser",customerReservation);
        return "myReservation";
    }

}
