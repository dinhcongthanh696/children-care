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

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int FEEDBACKSIZE = 6;
    private final ServiceModelService serviceModelService;
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    FeedbackService feedbackService;

    public ManagerController(ServiceModelService serviceModelService) {
        this.serviceModelService = serviceModelService;
    }

    @GetMapping("/service/{id}")
    public String gotoHtml(Model model,@PathVariable(name = "id") int id){
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services",serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
    }


    @RequestMapping(value = "/feedback", method = { RequestMethod.GET, RequestMethod.POST })
    public String searchServiceListByTitle() {

        return "manager-service-list";
    }
}
