package childrencare.app.API;



import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.RoleService;
import childrencare.app.service.ServiceModelService;
import childrencare.app.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminAPI {
	private final RoleService roleService;
	private final UserService userService;
	private final ServiceModelService serviceModelService;
	
	public AdminAPI(RoleService roleService,UserService userService,ServiceModelService serviceModelService) {
		this.roleService = roleService;
		this.userService = userService;
		this.serviceModelService = serviceModelService;
	}
	
	@PutMapping("/api-setting/users")
	@Transactional
	public void editUserRoleSettings(@RequestBody List<UserModel> users) {
		for(UserModel user : users) {
			 userService.updateUserRole(user);
		}
	}
	
	@PutMapping("/api-services")
	@Transactional
	public void editServiceStatus(@RequestBody ServiceModel service) {
		serviceModelService.editService(service);
	}
	
	@PostMapping("/api-services")
	@Transactional
	public void addNewService(@RequestParam(name = "title") String title,
			@RequestParam("thumbnail") MultipartFile thumbnail,@RequestParam("briefInfo") String briefInfo,
			@RequestParam("originalPrice") Integer originalPrice, @RequestParam("salePrice") Integer salePrice,
			@RequestParam("quantity") Integer quantity,@RequestParam("serviceCategoryId") Integer serviceCategoryId ,
			@RequestParam("description") String description,@RequestParam(name = "status") boolean status) {
		ServiceCategoryModel serviceCategoryModel = new ServiceCategoryModel();
		serviceCategoryModel.setServiceCategoryId(serviceCategoryId);
		ServiceModel service = null;
		try {
			service = new ServiceModel(thumbnail.getBytes(), title, briefInfo, originalPrice, salePrice, quantity, description, status, serviceCategoryModel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serviceModelService.addNewService(service);
	}
}
