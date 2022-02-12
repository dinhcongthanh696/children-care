package childrencare.app.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
	
	public UserModel findByEmail(String email);
	
	@Query(value = "SELECT * FROM user_model WHERE username LIKE ?1",nativeQuery = true)
	public Page<UserModel> findStudentByAttributes(String search,PageRequest pageRequest);
	
	@Modifying
	@Query(value = "UPDATE user_model SET role_id = ?1 WHERE username = ?2",nativeQuery = true)
	public void updateUserRole(Integer roleId,String username);
}
