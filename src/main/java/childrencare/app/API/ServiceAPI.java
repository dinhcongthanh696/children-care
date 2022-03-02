package childrencare.app.API;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.ServiceRepository;

@RestController
@RequestMapping("/api-service")
public class ServiceAPI {
	private final ServiceRepository serviceRepository;
	
	public ServiceAPI(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}
	
	@GetMapping("/get-user-services")
	public List<ServiceModel> getUserBoughtedServices(@RequestParam(name = "serviceId") Integer serviceId , HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		if(user == null || user.getCustomer() == null) {
			return new ArrayList<ServiceModel>();
		}
		return serviceRepository.findUserBoughtedServiceByServiceId(user.getEmail(), serviceId);
	}

	@PostMapping("/update-service")
	public String updateService(@RequestParam(name = "title")String title, @RequestParam(name = "briefinfo")String briefInfo,
								@RequestParam(name = "category")String category, @RequestParam(name = "originprice")double originPrice,
								@RequestParam(name = "saleprice")double salePrice, @RequestParam(name = "description")String description,
								@RequestParam(name = "id")int id){
		ServiceModel service = serviceRepository.getById(id);
		if(service != null){
			service.setTitle(title);
			service.setBriefInfo(briefInfo);
			service.setOriginalPrice(originPrice);
			service.setSalePrice(salePrice);
			service.setDescription(description);
			serviceRepository.save(service);
			return "success";

		}
		return "fail";

	}
}
