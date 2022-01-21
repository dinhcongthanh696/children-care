package childrencare.app.controller;

import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ServiceModelService serviceModelService;
    private final int size = 5;
    @Autowired
    public HomeController(ServiceModelService serviceModelService){
        this.serviceModelService = serviceModelService;
    }
    @GetMapping(path = "/")
    public String getServices(Model model){
        model.addAttribute("serviceitems",serviceModelService.getServices(size));
        return "index";
    }
}
