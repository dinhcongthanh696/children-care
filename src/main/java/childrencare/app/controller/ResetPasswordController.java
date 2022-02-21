package childrencare.app.controller;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/resetpass")
public class ResetPasswordController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/doreset")
    public String doReset(@RequestParam (name = "password") String password, HttpSession session){
        UserModel user = (UserModel) session.getAttribute("user");
        System.out.print(user == null);
        if(user != null) {
            user.setPassword(password);
            userRepository.save(user);
            return "redirect:/";
        }
        return "redirect:/resetpass";
    }
}
