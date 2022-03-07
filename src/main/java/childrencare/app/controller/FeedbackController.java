package childrencare.app.controller;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.FeedbackRepository;
import childrencare.app.service.FeedbackService;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String feedbackHome() {
        return "contact";
    }

    @PostMapping("/feedback")
    public void feedbackHome(@RequestParam("fullname") String fullname,
                               @RequestParam("gender") Integer get_gender,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("myfile") MultipartFile myfile,
                               @RequestParam(value = "rating", defaultValue = "0") Integer ratingstar,
                               @RequestParam("message") String comment) throws IOException {
        boolean gender = (get_gender == 1) ? true : false;
        byte[] imageBinaryData = (myfile == null) ? null : myfile.getBytes();
        feedbackService.saveGeneralFeedback(fullname, gender, email, phone, imageBinaryData, ratingstar, comment);
    }



    @GetMapping("/managerPage")
    public String getManagerScreen(){
        return "manager-service-list";
    }

}
