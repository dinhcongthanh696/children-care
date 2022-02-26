package childrencare.app.controller;


import childrencare.app.CaptchaGenerator;
import childrencare.app.configuration.MyConfiguration;
import childrencare.app.model.RoleModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@Controller
public class LoginController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/signIn", method = { RequestMethod.GET, RequestMethod.POST })
    public String signIn(@RequestParam(name = "currentPage", required = false , defaultValue = "/") String currentPage){
            return "redirect:"+currentPage;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/reset")
    public String reset(){
        return "resetpassword";
    }


}
