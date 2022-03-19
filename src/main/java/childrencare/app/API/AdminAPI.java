package childrencare.app.API;



import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import childrencare.app.model.RoleModel;
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
		serviceModelService.updateServiceStatus(service);
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

	@PostMapping("/api-users")
	@Transactional
	public void addNewUser(@RequestParam(name = "email")String email, @RequestParam(name = "gender")String gender,
						   @RequestParam(name = "fullname")String fullname, @RequestParam(name = "phone")String phone,
						   @RequestParam(name = "role")String role, @RequestParam(name = "status")String status,
						   @RequestParam(name = "file")MultipartFile file){
		RoleModel roleModel = new RoleModel();
		roleModel.setRoleId(Integer.parseInt(role));
		UserModel user = new UserModel();
		user.setEmail(email);
		user.setFullname(fullname);
		user.setPhone(phone);
		if(gender.equals("Male")){
			user.setGender(true);
		}else{
			user.setGender(false);
		}
		if(status.equals("Online")){
			user.setStatus(true);
		}else {
			user.setStatus(false);
		}
		user.setUserRole(roleModel);
		if(file != null){
			try {
				user.setAvatar(file.getBytes());
				user.setBase64AvaterEncode(user.getAvatar());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		userService.addNewUser(user);
	}
}
