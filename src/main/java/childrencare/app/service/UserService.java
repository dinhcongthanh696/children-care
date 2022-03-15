package childrencare.app.service;

import java.util.List;

import childrencare.app.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    
    
    public List<UserModel> findAllManager(){
        return userRepository.findUserModelByUserRole("manager");
    }
	
	public Page<UserModel> getPartialUsers(int page , int size , String search){
		if(page < 0) {
			page = 0;
		}
		Page<UserModel> users = userRepository.findUserByAttributes("%"+search+"%", PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "username")));
		if(users.getTotalPages() > 0 && page >= users.getTotalPages()) {
			page = users.getTotalPages() - 1;
			users = userRepository.findUserByAttributes("%"+search+"%", PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "username")));
		}
		return users;
	}
	
	public void updateUserRole(UserModel userModel) {
		userRepository.updateUserRole(userModel.getUserRole().getRoleId(), userModel.getUsername());
	}

	public UserModel findUserModelByUserReservationId(int reserId){
		return userRepository.findUserModelByUserReservationId(reserId);
	}

	public void updateInfo(String fullname, String phone, boolean gender, String email){
		userRepository.updateInfo(fullname, phone, gender, email);
	}

	public UserModel save(UserModel entity) {
		return userRepository.save(entity);
	}
	
	public UserModel findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Page<UserModel> getUserPageinately(String search, int page, int size,
													 int startBitRange, int endBitRange, List<String> sortProperties, Sort.Direction[] directions){
		if(page < 0) {
			page = 0;
		}
		Sort sortByMultipleProperties = Sort.by(directions[0], sortProperties.get(0))
				.and(Sort.by(directions[1], sortProperties.get(1)))
				.and(Sort.by(directions[2], sortProperties.get(2)))
				.and(Sort.by(directions[3], sortProperties.get(3)));

		Page<UserModel> usersPageable = userRepository.findUserByStatusAndSearchQuery("%"+search+"%",
				startBitRange,endBitRange,PageRequest.of(page,size,sortByMultipleProperties));
		if(usersPageable.getTotalPages() > 0 && page >= usersPageable.getTotalPages()) {
			page = usersPageable.getTotalPages() - 1;
			usersPageable = userRepository.findUserByStatusAndSearchQuery("%"+search+"%",
					startBitRange, endBitRange, PageRequest.of(page, size , sortByMultipleProperties ) );
		}

		return usersPageable;
	}
}
