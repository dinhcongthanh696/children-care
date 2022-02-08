package childrencare.app.API;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;

@RestController
@RequestMapping(path = "/api-user")
public class UserAPI {
	private final UserRepository userRepository;
	
	public UserAPI(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping(path = "/user")
	public UserModel getUserByEmail(@RequestParam(name = "email") String email) {
		return userRepository.findByEmail(email);
	}
}
