package childrencare.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Page<UserModel> getPartialUsers(int page , int size , String search){
		if(page < 0) {
			page = 0;
		}
		Page<UserModel> users = userRepository.findStudentByAttributes("%"+search+"%", PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "username")));
		if(users.getTotalPages() > 0 && page >= users.getTotalPages()) {
			page = users.getTotalPages() - 1;
			users = userRepository.findStudentByAttributes("%"+search+"%", PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "username")));
		}
		return users;
	}
	
	public void updateUserRole(UserModel userModel) {
		userRepository.updateUserRole(userModel.getUserRole().getRoleId(), userModel.getUsername());
	}
}
