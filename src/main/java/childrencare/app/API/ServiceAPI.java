package childrencare.app.API;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public List<ServiceModel> getUserFeedback(@RequestParam(name = "serviceId") Integer serviceId , HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		if(user == null) {
			return new ArrayList<ServiceModel>();
		}
		return serviceRepository.findByUserEmailAndServiceId(user.getEmail(), serviceId);
	}
}
