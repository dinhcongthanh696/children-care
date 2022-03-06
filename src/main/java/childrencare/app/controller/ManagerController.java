package childrencare.app.controller;

import childrencare.app.model.CustomerModel;
import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.CustomerService;
import childrencare.app.service.FeedbackService;
import childrencare.app.service.ServiceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int FEEDBACKSIZE = 6;
    
    private final int CUSTOMERSIZE = 6;
    @Autowired
    private final ServiceModelService serviceModelService;
    
    private final CustomerService customerService;
    
    @Autowired
    ServiceRepository serviceRepository;
    private final List<String> sortProperties = Arrays.asList("email","fullname","phone","status");

    @Autowired
    FeedbackService feedbackService;
    
    @Autowired
    public ManagerController(ServiceModelService serviceModelService,CustomerService customerService) {
        this.serviceModelService = serviceModelService;
        this.customerService = customerService;
    }

    @GetMapping("/service/{id}")
    public String gotoHtml(Model model,@PathVariable(name = "id") int id){
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services",serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
    }
    
    @GetMapping("/customers")
    public String toCustomersList(@RequestParam(name = "search" , required = false , defaultValue = "") String search ,
    		@RequestParam(name = "status" , required = false , defaultValue = "-1") int status , 
    		@RequestParam(name = "direction" , required = false , defaultValue = "descending") String directionValue,
    		@RequestParam(name = "sortProperty" , required = false , defaultValue = "email") String sortProperty ,
    		@RequestParam(name = "page" , required = false , defaultValue = "0") int page,
    		Model model) {
    	int startBitRange = -1;
    	int endBitRange = 2;
    	if(status == 0) {
    		endBitRange -= 1;
    	}else {
    		startBitRange += 1;
    	}
    	
    	Direction direction = (directionValue.equals("descending")) ? Direction.DESC : Direction.ASC;
    	Collections.swap(sortProperties, sortProperties.indexOf(sortProperty), 0);
    	Page<CustomerModel> customersPageable = customerService.getCustomerPageinately(search, page, CUSTOMERSIZE, startBitRange, endBitRange, sortProperties, direction);
    	
    	model.addAttribute("sortProperty",sortProperty);
    	model.addAttribute("customers", customersPageable.toList());
    	model.addAttribute("currentPage", customersPageable.getNumber());
    	model.addAttribute("totalPages", customersPageable.getTotalPages());
    	model.addAttribute("direction", directionValue);
    	model.addAttribute("search",search);
    	model.addAttribute("status", status);
    	model.addAttribute("sortProperty", sortProperty);
    	
    	return "manager-customer-list";
    }


    @RequestMapping(value = "/feedback", method = { RequestMethod.GET, RequestMethod.POST })
    public String searchServiceListByTitle() {

        return "manager-service-list";
    }
}
