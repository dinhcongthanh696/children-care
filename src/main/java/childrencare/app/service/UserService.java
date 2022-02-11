package childrencare.app.service;

import childrencare.app.model.UserModel;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserModel> findAllDoctor(){
        return userRepository.findUserModelByUserRole("doctor");
    }

}
