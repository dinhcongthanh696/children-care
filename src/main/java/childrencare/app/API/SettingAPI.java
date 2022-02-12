package childrencare.app.API;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import childrencare.app.model.UserModel;
import childrencare.app.service.RoleService;
import childrencare.app.service.UserService;

@RestController
@RequestMapping("/api-setting")
public class SettingAPI {
	private final RoleService roleService;
	private final UserService userService;
	
	public SettingAPI(RoleService roleService,UserService userService) {
		this.roleService = roleService;
		this.userService = userService;
	}
	
	@PutMapping("/users")
	@Transactional
	public void editUserRoleSettings(@RequestBody List<UserModel> users) {
		for(UserModel user : users) {
			 userService.updateUserRole(user);
		}
	}
}
