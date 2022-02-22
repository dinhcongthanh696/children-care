package childrencare.app.controller;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/update")
    public String updateProfile(@RequestParam(name = "address") String address, @RequestParam(name = "fullname") String fullname,
                                @RequestParam(name = "phone") String phone, HttpSession session){
        UserModel user = (UserModel) session.getAttribute("user");
        if(user != null){
            user.setAddress(address);
            user.setFullname(fullname);
            user.setPhone(phone);
            userRepository.save(user);
            return "redirect:/profile/update";
        }
        return "redirect:/";

    }
}
