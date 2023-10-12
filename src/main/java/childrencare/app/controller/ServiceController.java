package childrencare.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import childrencare.app.repository.*;
import childrencare.app.service.*;
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
import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;

@Controller
@RequestMapping("/service")
public class ServiceController {
	private final ServiceCategoryRepository serviceCategoryRepository;
	private final ServiceModelService serviceModelService;
	private final ServiceCategoryService serviceCategoryService;
	private final FeedbackRepository feedbackRepository;
	private final CustomerService customerService;
	private final ReservationService reservationService;
	@Autowired
	private BlogCategoryService blogCategoryService;
	private final int SERVICESIZE = 9;
	@Autowired
	public ServiceController(ServiceCategoryRepository serviceCategoryRepository,
							 ServiceModelService serviceModelService, ServiceCategoryService serviceCategoryService,
							  FeedbackRepository feedbackRepository, CustomerService customerService ,
							 ReservationService reservationService) {
		this.serviceCategoryRepository = serviceCategoryRepository;
		this.serviceModelService = serviceModelService;
		this.serviceCategoryService = serviceCategoryService;
		this.feedbackRepository = feedbackRepository;
		this.customerService = customerService;
		this.reservationService = reservationService;
	}
	
	@RequestMapping(value = "/services", method = { RequestMethod.GET, RequestMethod.POST })
	public String getServices(Model model,
			@RequestParam(name =  "page",required = false, defaultValue = "0") int page,
			@RequestParam(name =  "search",required = false, defaultValue = "") String search, 
			@RequestParam(name = "category",required = false, defaultValue =  "0") int categoryId,
			@RequestParam(name = "lang",required = false, defaultValue = "en") String lang ,
			HttpSession session) {
		Page<ServiceModel> services = null;
		int startBitRange = 0;
		int endBitRange = 2;   // Collect service that is showing
		if(categoryId != 0) {
			services = serviceModelService.getServicesPaginated(page, SERVICESIZE, categoryId, startBitRange , endBitRange, search);
		}else {
			services = serviceModelService.getServicesPaginated(page, SERVICESIZE, startBitRange, endBitRange, search, "title");
		}

		List<ServiceCategoryModel> categories = serviceCategoryService.findAll();
		List<ReservationServiceModel> reservationServices = new ArrayList<>();
		if(session != null) {
			UserModel user = (UserModel) session.getAttribute("user");
			if(user != null && user.getCustomer() != null) {
				for(ReservationModel reservation : reservationService.listReservationByCusID(user.getCustomer().getCustomer_id())) {
					for(ReservationServiceModel reservationService : reservation.getReservationServices()) {
						reservationServices.add(reservationService);
					}
				}
			}else if(user != null && user.getCustomer() == null){
				user.setCustomer(customerService.findCustomerByEmail(user.getEmail()));
				if(user.getCustomer() != null) {
					for (ReservationModel reservation : reservationService.listReservationByCusID(user.getCustomer().getCustomer_id())) {
						for (ReservationServiceModel reservationService : reservation.getReservationServices()) {
							reservationServices.add(reservationService);
						}
					}
				}
			}
		}
		
		model.addAttribute("services",services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("search", search);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("servicecategories", categories);
		model.addAttribute("listCategoryPost", blogCategoryService.findAll());
		model.addAttribute("userReservationServices", reservationServices);
		model.addAttribute("lang",lang);
		return "service-list";
	}

	@Autowired
	UserRepository userRepository;

	@GetMapping(path = "/services/{id}")
	public String getServiceById(Model model, @PathVariable(name = "id") int id ,
			@RequestParam(name = "lang",required = false, defaultValue = "en") String lang) {
		List<ServiceCategoryModel> serviceCategory = serviceCategoryRepository.findAll();
		ServiceModel service = serviceModelService.getServiceById(id).get();
		service.setBase64ThumbnailEncode(service.getThumbnail());
		List<FeedbackModel> feedbackModels = feedbackRepository.findByService(service);
		UserModel userModel;
		for(FeedbackModel feedback : feedbackModels) {
			userModel = feedback.getCustomer().getCustomer_user();
			if(feedback.getImage() != null) {
				feedback.setBase64ImageEncode(feedback.getImage());
				userModel.setBase64AvaterEncode(userModel.getAvatar());
			}
		}
		model.addAttribute("servicecategories", serviceCategory);
		model.addAttribute("service", service);
		model.addAttribute("services",serviceModelService.getServices());
		model.addAttribute("feedbackModels", feedbackModels);
		model.addAttribute("lang", lang);
		return "ServiceDetail";
	}
}

