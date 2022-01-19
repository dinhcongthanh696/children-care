package childrencare.app.controller;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ServiceController {
    private final ServiceRepository serviceRepository;
    @Autowired
    public ServiceController(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }
    @GetMapping("/service/{id}")
    public String getServiceById(@PathVariable(name = "id") int id, Model model){
        ServiceModel service = serviceRepository.findById(id).get();
        model.addAttribute("service", service);
        return "ServiceDetail";
    }
}
