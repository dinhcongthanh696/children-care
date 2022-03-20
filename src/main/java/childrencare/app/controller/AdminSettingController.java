package childrencare.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import childrencare.app.model.PermissionKey;
import childrencare.app.model.PermissionModel;
import childrencare.app.model.RoleModel;
import childrencare.app.model.ScreenModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.PermissionService;
import childrencare.app.service.RoleService;
import childrencare.app.service.ScreenService;
import childrencare.app.service.UserService;

@Controller
@RequestMapping("/admin/setting")
public class AdminSettingController {
	private final RoleService roleService;
	private final ScreenService screenService;
	private final PermissionService permissionService;
	private final UserService userService;
	private final int SCREENSIZE = 20;
	private final int USERSIZE = 3;
	
	@Autowired
	public AdminSettingController(RoleService roleService,ScreenService screenService,PermissionService permissionService,UserService userService) {
		this.roleService = roleService;
		this.screenService = screenService;
		this.permissionService = permissionService;
		this.userService = userService;
	}
	
	@RequestMapping(value = "/roles",method = {RequestMethod.GET,RequestMethod.POST})
	public String toSettingRole(Model model , @RequestParam(name = "page" , required = false , defaultValue = "0") int page,
			@RequestParam(name = "search" , required = false , defaultValue = "") String search, 
			@RequestParam(name = "currentRoleIndex" , required = false , defaultValue = "0") int roleIndex) {
		List<RoleModel> roles = roleService.getAllRoles();
		Page<ScreenModel> currentScreens = screenService.getPartialScreens(SCREENSIZE, page, search);
		model.addAttribute("roles", roles);
		model.addAttribute("screens",currentScreens.toList());
		model.addAttribute("currentPage", currentScreens.getNumber());
		model.addAttribute("totalPages", currentScreens.getTotalPages());
		model.addAttribute("currentRole", roles.get(roleIndex));
		model.addAttribute("search", search);
		return "setting-role";
	}
	
	@RequestMapping(value = "/users",method = {RequestMethod.GET,RequestMethod.POST})
	public String toSettingUser(Model model , @RequestParam(name = "page" , required = false , defaultValue = "0") int page,
			@RequestParam(name = "search" , required = false , defaultValue = "") String search
			) {
		List<RoleModel> roles = roleService.getAllRoles();
		Page<UserModel> users = userService.getPartialUsers(page, USERSIZE, search);
		model.addAttribute("roles", roles);
		model.addAttribute("users", users.toList());
		model.addAttribute("currentPage", users.getNumber());
		model.addAttribute("totalPages", users.getTotalPages());
		model.addAttribute("search",search);
		return "setting-user";
	}
	
	@PostMapping("/roles/edit")
	@Transactional
	public String editRoleScreens(@RequestParam(name = "permissionValues") String permissionValues ,
								  @RequestParam(name = "roleId") int roleId ,
								  @RequestParam(name = "screenIdValues") String screenIdValues, HttpSession session) {

		String[] permissionValues_split = permissionValues.split("[,]");
		String[] screenIdValues_split = screenIdValues.split("[,]");
		List<Integer> currentScreens = new ArrayList<>();
		for(String screenIdValue : screenIdValues_split){
			currentScreens.add(Integer.parseInt(screenIdValue));
		}
		permissionService.removePermissionByRole(roleId,currentScreens);
		for(int i = 0 ; i < permissionValues_split.length ; i++) {
			if(!permissionValues_split[i].equals("ban")) {
				permissionService.addPermission(roleId, Integer.parseInt(permissionValues_split[i]));
			}
		}

		UserModel userModel = (UserModel) session.getAttribute("user");
		userModel.getUserRole().setPermissions(permissionService.loadByRole(roleId));
		return "redirect:/setting/roles";
	}
}
