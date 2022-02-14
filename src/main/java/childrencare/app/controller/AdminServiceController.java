package childrencare.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceCategoryService;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/admin")
public class AdminServiceController {
	private final ServiceModelService serviceModelSerivce;
	private final int SERVICESIZE = 6;
	private final ServiceCategoryService serviceCategoryService;
	
	public AdminServiceController(ServiceModelService serviceModelService,ServiceCategoryService serviceCategoryService) {
		this.serviceModelSerivce = serviceModelService;
		this.serviceCategoryService = serviceCategoryService;
	}
	
	
	@RequestMapping(value = "/services", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchServiceListByTitle(Model model,
			@RequestParam(name =  "page",required = false, defaultValue = "0") int page,
			@RequestParam(name =  "search",required = false, defaultValue = "") String search,
			@RequestParam(name = "status",required = false,defaultValue = "") String rawStatus,
			@RequestParam(name = "sort" , required = false , defaultValue = "title") String sort) {
		Page<ServiceModel> services;
		int startBitRangeValue = -1;
		int endBitRangeValue = 2;
		if(rawStatus.equals("true")) {
			startBitRangeValue = 0;
		}else if(rawStatus.equals("false")) {
			endBitRangeValue = 1;
		}
		services = serviceModelSerivce.getServicesPaginated(page, SERVICESIZE, startBitRangeValue, endBitRangeValue, search,sort);
		
		for(ServiceModel service : services) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
			double averageStars = 0;
			for(FeedbackModel feedback : service.getFeedbacks()) {
				averageStars += feedback.getRatedStart();
			}
			if(averageStars != 0) {
				averageStars /= service.getFeedbacks().size(); 
				service.setAvg_star(averageStars);
			}
		}
		List<ServiceCategoryModel> categories = serviceCategoryService.findAll();
		
		model.addAttribute("services",services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("search", search);
		model.addAttribute("status", rawStatus);
		model.addAttribute("categories", categories);
		model.addAttribute("sort", sort);
		return "manager-service-list";
	}
	
}
