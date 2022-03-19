package childrencare.app.controller;

import childrencare.app.model.CustomerModel;
import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.FeedbackRepository;
import childrencare.app.service.CustomerService;
import childrencare.app.service.FeedbackService;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
public class FeedbackController {
    private final ServiceModelService serviceModelService;
    private final FeedbackService feedbackService;


    public JavaMailSender emailSender;

    @Autowired
    public FeedbackController(ServiceModelService serviceModelService, FeedbackService feedbackService) {
        this.serviceModelService = serviceModelService;
        this.feedbackService = feedbackService;
    }

    @Autowired
    private CustomerService customerService;

    @ResponseBody
    @RequestMapping("/sendSimpleEmail")
    public String sendSimpleEmail() {

        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("anhbdhe151175@fpt.edu.vn");
        message.setSubject("Test Simple Email");
        message.setText("Hello, Im testing Simple Email");

        // Send Message!
        this.emailSender.send(message);

        return "Email Sent!";
    }

    @GetMapping("/feedback")
    public String feedbackHome(Model model, HttpSession session) {
        UserModel userModel = (UserModel) session.getAttribute("user");
        model.addAttribute("user", userModel);
        return "contact";
    }

    @PostMapping("/feedback")
    @Transactional
    public void saveFeedback(
            @RequestParam(name = "cus_email", defaultValue = "empty_email") String email,
            @RequestParam(name = "create_date") String date,
            @RequestParam(name = "image") MultipartFile img,
            @RequestParam(name = "rating", defaultValue = "0") Integer ratingstar,
            @RequestParam(name = "message") String comment) throws IOException {

        int customer_id;
        byte[] imageBinaryData = (img == null) ? null : img.getBytes();
        if (!email.equals("empty_email")) {
            customer_id = customerService.getCusIDByEmail(email);
            feedbackService.saveGeneralFeedback(comment, date, imageBinaryData, ratingstar, true, customer_id);
        } else {
            feedbackService.saveGeneralFeedback(comment, date, imageBinaryData, ratingstar, true);
        }
    }


    @GetMapping("/managerPage")
    public String getManagerScreen() {
        return "manager-service-list";
    }

}
