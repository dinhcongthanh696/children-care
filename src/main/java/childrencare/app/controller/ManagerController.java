package childrencare.app.controller;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.FeedbackService;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int PAGE_SIZE = 6;
    private final ServiceModelService serviceModelService;


    private final FeedbackService feedbackService;

    public ManagerController(ServiceModelService serviceModelService,
                             FeedbackService feedbackService) {
        this.serviceModelService = serviceModelService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/service/{id}")
    public String gotoHtml(Model model,@PathVariable(name = "id") int id){
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services",serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
    }


    @GetMapping("/feedback")
    public String filterFeedback(Model model,
          @RequestParam(name="page", required = false, defaultValue = "0") Integer page,
          @RequestParam(name="serviceId", required = false, defaultValue = "-1") Integer sid,
          @RequestParam(name="numberOfStar", required = false, defaultValue = "-1") Integer numberOfStar,
          @RequestParam(name="status", required = false, defaultValue = "-1") Integer status,
          @RequestParam(name="content", required = false, defaultValue = "") String content,
          @RequestParam(name="contactName", required = false, defaultValue = "") String contactName
    ){

        Page<FeedbackModel> feedbacks = feedbackService.getPaginatedFeedback
                (page - 1, 3, sid, numberOfStar, contactName.trim(), content.trim(), status);
        model.addAttribute("feedbacks", feedbacks.toList());


        int totalPages = feedbacks.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, page - 2);
            int end = Math.min(page+ 2, totalPages - 1);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 5;
                else if (start == 1) end = start + 5;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        List<ServiceModel> services = serviceModelService.getServices();
        model.addAttribute("serviceList", services);
        model.addAttribute("feedbackPage", feedbacks);

        model.addAttribute("serviceId", sid);
        model.addAttribute("numberOfStar", numberOfStar);
        model.addAttribute("status", status);
        model.addAttribute("content", content);
        model.addAttribute("contactName", contactName);

        return "manager-feedback-list";
    }

}
