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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

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
    @Transactional
    public String toCustomersList(@RequestParam(name = "search" , required = false , defaultValue = "") String search ,
    		@RequestParam(name = "status" , required = false , defaultValue = "-1") int status , 
    		@RequestParam(name = "directions" , required = false , defaultValue = "ascending,ascending,ascending,ascending") String directionsParam,
    		@RequestParam(name = "sortProperty" , required = false , defaultValue = "email") String sortProperty ,
    		@RequestParam(name = "page" , required = false , defaultValue = "0") int page,
    		Model model) {
    	int startBitRange = -1;
    	int endBitRange = 2;
    	switch(status) {
    		case 0 : endBitRange--;break;
    		case 1 : startBitRange++;break;
    	}
    	
    	String[] directionsValue = directionsParam.split("[,]");
    	Direction[] directions = new Direction[directionsValue.length];
    	for(int i = 0 ; i < directionsValue.length ; i++) {
    		directions[i] = (directionsValue[i].equals("ascending")) ? Direction.ASC : Direction.DESC;
    	}
    	List<String> sortProperties = Arrays.asList("u.email","u.fullname","u.phone","u.status");
    	Collections.swap(sortProperties, sortProperties.indexOf("u."+sortProperty), 0);
    	Page<CustomerModel> customersPageable = customerService.getCustomerPageinately(search, page, CUSTOMERSIZE, startBitRange, endBitRange, sortProperties, directions);
    	model.addAttribute("customers", customersPageable.toList().stream().map(customer -> {
    		if(customer.getCustomer_user().getAvatar() != null)
    		customer.getCustomer_user().setBase64AvatarEncode(
    				Base64
    				.getEncoder().
    				encodeToString(customer.getCustomer_user().getAvatar()));
    		return customer;
    	}).toList()
    	);
    	model.addAttribute("currentPage", customersPageable.getNumber());
    	model.addAttribute("totalPages", customersPageable.getTotalPages());
    	model.addAttribute("directionsValue", directionsValue);
    	model.addAttribute("directionsParam", directionsParam);
    	model.addAttribute("search",search);
    	model.addAttribute("status", status);
    	model.addAttribute("sortProperty", sortProperty);
    	model.addAttribute("sortProperties", sortProperties);
    	
    	return "manager-customer-list";
    }


    @RequestMapping(value = "/feedback", method = { RequestMethod.GET, RequestMethod.POST })
    public String searchServiceListByTitle() {

        return "manager-service-list";
    }
}
