package childrencare.app.API;

import childrencare.app.model.RoleModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.LoginRepository;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@RestController
@RequestMapping("/api-login")
public class LoginAPI {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;


    @PostMapping("/reset")
    public String sendResetLink(@RequestParam(name = "email") String email){
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
        String from = mailSenderImpl.getUsername();
        String to = email;
        String subject = "Reset Password from ChildrenCare";
        MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
        try {
            mimeMessage.setFrom(from);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            MimeBodyPart contentPart = new MimeBodyPart();
            String content = "<h1> Hello "+ email +" , Reset link is : <a href='http://localhost:8080/ChildrenCare/reset'>link</a> </h1>" ;
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
            return "Fail";
        }
        return "Success";
    }
    @PostMapping(value = "/signIn")
    public String signIn(Model model, HttpSession session,
                         @RequestParam(name = "email")String input,
                         @RequestParam(name = "password")String password,
                         @RequestParam(name = "currentPage", required = false , defaultValue = "/") String currentPage){
        UserModel userExist = loginRepository.checkUserExist(input,password);
        if(userExist != null && userExist.isStatus()){
            session.setAttribute("user",userExist);
            if(userExist.getAvatar() != null){
                userExist.setBase64AvatarEncode(Base64.getEncoder().encodeToString(userExist.getAvatar()));
                session.setAttribute("username",userExist.getUsername());
                session.setAttribute("email",userExist.getEmail());
                return "Đăng nhập thành công";
            }
        }
        else if(!userExist.isStatus()){
            return "Tài khoản của bạn bị vô hiệu hoá";
        }
        else{
            return "Tài khoản hoặc mật khẩu không chính xác";
        }
        return "";
    }

    @PostMapping(value = "/signUp")
    public String signUp(Model model,
                         @RequestParam(name = "username")String username,
                         @RequestParam(name = "fullname")String fullname,
                         @RequestParam(name = "address")String address,
                         @RequestParam(name = "email")String email,
                         @RequestParam(name = "gender") boolean gender,
                         @RequestParam(name = "notes")String notes,
                         @RequestParam(name = "password")String pass,
                         @RequestParam(name = "phone")String phone) {
        UserModel userExist1 = loginRepository.checkUserRegister(email, username);
        if (userExist1 != null) {
            return "Email hoặc username đã tồn tại!";
        } else {
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
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        userModel.setRegiteredDate(dtf.format(now));
        loginRepository.save(userModel);
        return "Đăng kí tài khoản thành công! Đăng nhập để sử dụng Website";
    }
    }
}
