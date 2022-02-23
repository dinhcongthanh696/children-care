package childrencare.app.API;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(path = "/api-user")
public class UserAPI {
	private final UserRepository userRepository;
	
	public UserAPI(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping(path = "/user")
	public String getUserByEmail(@RequestParam(name = "email") String email, HttpSession session) {
		UserModel user = userRepository.findByEmail(email);
		if(user != null){
			session.setAttribute("user", user);
			return email;
		} else{
			return "not exist";
		}
	}

	@PostMapping("/update")
	public String updateProfile(@RequestParam(name = "address") String address, @RequestParam(name = "fullname") String fullname,
								@RequestParam(name = "phone") String phone, @RequestParam(name = "gender")String gender ,
								@RequestParam(name = "avatar", required = false) MultipartFile file , HttpSession session){
		UserModel user = (UserModel) session.getAttribute("user");
		if(user != null){
			user.setAddress(address);
			user.setFullname(fullname);
			user.setPhone(phone);
			if(gender.equals("Nam")){
				user.setGender(true);
			}else{
				user.setGender(false);
			}
			if(file != null){
				try {
					user.setAvatar(file.getBytes());
					user.setBase64AvatarEncode(Base64.getEncoder().encodeToString(user.getAvatar()));
				} catch (IOException e) {
					return "fail";
				}
			}
			userRepository.save(user);
			return "success";
		}
		return "fail";

	}
}
