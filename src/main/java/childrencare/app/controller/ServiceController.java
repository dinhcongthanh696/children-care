package childrencare.app.controller;

import java.util.List;

import childrencare.app.repository.FeedbackRepository;
import childrencare.app.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceCategoryService;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/service")
public class ServiceController {
	private final ServiceCategoryRepository serviceCategoryRepository;
	private final ServiceModelService serviceModelService;
	private final ServiceCategoryService serviceCategoryService;
	private final FeedbackRepository feedbackRepository;
	private final int SERVICESIZE = 3;
	@Autowired
	public ServiceController(ServiceCategoryRepository serviceCategoryRepository, ServiceModelService serviceModelService, ServiceCategoryService serviceCategoryService, FeedbackRepository feedbackRepository) {
		this.serviceCategoryRepository = serviceCategoryRepository;
		this.serviceModelService = serviceModelService;
		this.serviceCategoryService = serviceCategoryService;
		this.feedbackRepository = feedbackRepository;
	}
	
	@RequestMapping(value = "/services", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchServiceListByTitle(Model model,
			@RequestParam(name =  "page",required = false, defaultValue = "0") int page,
			@RequestParam(name =  "search",required = false, defaultValue = "") String search, 
			@RequestParam(name = "category",required = false, defaultValue =  "0") int categoryId) {
		Page<ServiceModel> services = null;
		if(categoryId != 0) {
			services = serviceModelService.getServicesPaginated(page, SERVICESIZE, categoryId, search);
		}else {
			services = serviceModelService.getServicesPaginated(page, SERVICESIZE, search , "title");
		}

		List<ServiceCategoryModel> categories = serviceCategoryService.findAll();
		
		model.addAttribute("services",services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("search", search);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("servicecategories", categories);
		model.addAttribute("categoryId",categoryId);
		return "service-list";
	}
	
	@GetMapping(path = "/services/{id}")
	public String getServiceById(Model model, @PathVariable(name = "id") int id) {
		List<ServiceCategoryModel> serviceCategory = serviceCategoryRepository.findAll();
		ServiceModel service = serviceModelService.getServiceById(id).get();
		service.setBase64ThumbnailEncode(service.getThumbnail());
		List<FeedbackModel> feedbackModels = feedbackRepository.findByService(service);
		for(FeedbackModel feedback : feedbackModels) {
			feedback.setBase64ImageEncode(feedback.getImage());
		}
		model.addAttribute("servicecategories", serviceCategory);
		model.addAttribute("service", service);
		model.addAttribute("services",serviceModelService.getServices());
		model.addAttribute("feedbackModels", feedbackModels);
		return "ServiceDetail";
	}
}

