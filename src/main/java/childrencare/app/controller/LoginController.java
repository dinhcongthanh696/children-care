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

@Controller
public class LoginController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/signIn", method = { RequestMethod.GET, RequestMethod.POST })
    public String signIn(Model model, HttpSession session,
                         @RequestParam(name = "email")String input,
                         @RequestParam(name = "password")String password,
                         @RequestParam(name = "currentPage", required = false , defaultValue = "/") String currentPage){
        UserModel userExist = loginService.checkUserExist(input,input,password);
        if(userExist != null){
            model.addAttribute("user",userExist);
            session.setAttribute("user",userExist);
            session.setAttribute("username",userExist.getUsername());
            session.setAttribute("email",userExist.getEmail());
            return "redirect:"+currentPage;
        }else{
            model.addAttribute("mess","Wrong information");
            return "redirect:"+currentPage;
        }

    }

    @RequestMapping(value = "/signUp", method = { RequestMethod.GET, RequestMethod.POST })
    public String signUp(Model model,
                         @RequestParam(name = "username")String username,
                         @RequestParam(name = "fullname")String fullname,
                         @RequestParam(name = "address")String address,
                         @RequestParam(name = "email")String email,
                         @RequestParam(name = "gender") boolean gender,
                         @RequestParam(name = "notes")String notes,
                         @RequestParam(name = "password")String pass,
                         @RequestParam(name = "phone")String phone){
        UserModel userExist1 = loginService.checkUserExist(username,email,"");
        if(userExist1 != null){
            model.addAttribute("messError","Information exist. Please change!");
        }
        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setFullname(fullname);
        userModel.setAddress(address);
        userModel.setEmail(email);
        userModel.setGender(gender);
        userModel.setNotes(notes);
        userModel.setPassword(pass);
        userModel.setPhone(phone);
        userModel.setStatus(true);
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleId(4);
        userModel.setUserRole(roleModel);
        loginService.save(userModel);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String sendResetLink(@RequestParam(name = "email") String email){
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
        String from = mailSenderImpl.getUsername();
        String to = email;
        String subject = "Email identifycation from Children Care";
        MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
        try {
            mimeMessage.setFrom(from);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            MimeBodyPart contentPart = new MimeBodyPart();
            String content = "<h1> Hello "+ email +" , Reset link is : <a href='/ChildrenCare/reset'>link</a> </h1>" ;
            contentPart.setContent(content, "text/html; charset=utf-8");

            MimeBodyPart referencePart = new MimeBodyPart();
            String reference = "<a href='http://localhost:8080/ChildrenCare/'> Children Care's Page</a>";
            referencePart.setContent(reference,"text/html; charset=utf-8");

            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(contentPart);
            multiPart.addBodyPart(referencePart);

            mimeMessage.setContent(multiPart);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/signIn";
    }


}
