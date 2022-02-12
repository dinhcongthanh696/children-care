package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.UserModel;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
	
	public UserModel findByEmail(String email);

	@Query(value = "Select * from user_model u inner join role r " +
			"on u.role_id = r.role_id where r.role_name = ?1", nativeQuery = true)
	public List<UserModel> findUserModelByUserRole(String roleName);

	@Query(value = "Update user_model set fullname = ?1, phone = ?2, gender = ?3\n" +
			"where email = ?4", nativeQuery = true)
	public void updateInfo(String fullName, String phone, boolean gender, String email);


//	public List<UserModel> getAvailableDoctor(Date date);
}
