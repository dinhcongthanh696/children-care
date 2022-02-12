package childrencare.app.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/api-login")
public class LoginAPI {
    @Autowired
    private JavaMailSender mailSender;


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
            return "Fail";
        }
        return "Success";
    }
}
