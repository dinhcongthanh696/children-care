package childrencare.app.controller;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.LoginService;
import childrencare.app.service.ReservationService_Service;
import childrencare.app.service.SlidersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.awt.datatransfer.MimeTypeParseException;
import java.awt.print.Pageable;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/setting")
public class UserSettingController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ReservationService_Service reservationService_service;

    @Autowired
    private SlidersService slidersService;



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
    @GetMapping("/myReservation/page/{pageNum}")
    public String getmyReservation(Model model,
                                   @PathVariable(name = "pageNum") int pageNum,
                                   HttpSession session){
        Page<ReservationServiceModel> page = reservationService_service.listAll(pageNum);
        UserModel user = (UserModel) session.getAttribute("user");
        List<ReservationServiceModel> customerReservation = reservationService_service.getCustomerReservation(user.getEmail());
        model.addAttribute("userLogin",user);
        model.addAttribute("customerReser",customerReservation);
        return "myReservation";
    }

    @GetMapping("/sliderManager/page/{pageNum}")
    public String viewPage(Model model,@PathVariable(name ="pageNum") int pageNum){
        Page<SliderModel> page = slidersService.listAll(pageNum);
        List<SliderModel> slidersList = page.getContent();
        for (SliderModel slider: slidersList) {
            slider.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(slider.getImage()));
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("slidersList",slidersList);

        return "sliders-manager";
    }

    @GetMapping ("/changStatus/{id}")
    @Transactional
    public String changStatusSlider( @PathVariable(value = "id") int id,Model model){
        slidersService.updatestatusSlider(true,id);
        return "redirect:/setting/profile";
    }

    @PostMapping("/update")
    @Transactional
    public String updateSlider(@RequestParam(name = "id") int id,
                               @RequestParam(name = "title") String title,
                               @RequestParam(name = "backlink") String backlink,
                               @RequestParam(name = "status") boolean status,
                               @RequestParam(name = "note") String note){
        slidersService.updateSlider(backlink,note,status,title,id);
        return "redirect:/setting/sliderManager";
    }




}
