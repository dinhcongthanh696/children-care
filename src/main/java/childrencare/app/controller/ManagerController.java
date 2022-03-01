package childrencare.app.controller;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final ServiceModelService serviceModelService;
    @Autowired
    ServiceRepository serviceRepository;

    public ManagerController(ServiceModelService serviceModelService) {
        this.serviceModelService = serviceModelService;
    }

    @GetMapping("/servicedetail")
    public String gotoHtml(Model model){
        model.addAttribute("services",serviceModelService.getServices());
        return "servicedetail-manager";
    }
}
