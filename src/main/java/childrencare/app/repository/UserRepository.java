package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import childrencare.app.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
	
	
	public UserModel findByEmail(String email);
}
