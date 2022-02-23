package childrencare.app.controller;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import childrencare.app.service.LoginService;
import childrencare.app.service.ReservationService_Service;
import childrencare.app.service.SlidersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.awt.datatransfer.MimeTypeParseException;
import java.awt.print.Pageable;
import java.io.IOException;
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
    private UserRepository userRepository;

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
        UserModel user = (UserModel) session.getAttribute("user");
        String email = user.getEmail();
        List<ReservationServiceModel> customerReservation = reservationService_service.findAllByEmail(email);
        Page<ReservationServiceModel> page = reservationService_service.listAll(pageNum);
        customerReservation = page.getContent();
        model.addAttribute("userLogin",user);
        model.addAttribute("customerReser",customerReservation);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "myReservation";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam(name = "address") String address, @RequestParam(name = "fullname") String fullname,
                                @RequestParam(name = "phone") String phone,@RequestParam(name = "gender")String gender ,
                                @RequestParam(name = "avatar")MultipartFile file ,HttpSession session){
        UserModel user = (UserModel) session.getAttribute("user");
        if(user != null){
            user.setAddress(address);
            user.setFullname(fullname);
            user.setPhone(phone);
            if(gender.equals("Nam")){
                user.setGender(true);
            }else{
                user.setGender(false);
            }
            if(file != null){
                try {
                    user.setAvatar(file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userRepository.save(user);
            return "redirect:/setting/profile";
        }
        return "redirect:/";

    }



}
