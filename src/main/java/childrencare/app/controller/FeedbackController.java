package childrencare.app.controller;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.FeedbackRepository;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FeedbackController {
    private final ServiceModelService serviceModelService;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public JavaMailSender emailSender;

    public FeedbackController(ServiceModelService serviceModelService, FeedbackRepository feedbackRepository) {
        this.serviceModelService = serviceModelService;
        this.feedbackRepository = feedbackRepository;
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

    /* @GetMapping(path = "/services/{id}")
    public String getServiceFeedback(Model model, @PathVariable(name = "id") int id){
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceId(id);
        List<FeedbackModel> feedbackModels = feedbackRepository.findByService(serviceModel);
        for (FeedbackModel feedbackModel:feedbackModels
             ) {
            System.out.print(feedbackModel.getComment());
        }
        model.addAttribute("feedbackModels", feedbackModels);
        return "ServiceDetail";
    }
    */


}
