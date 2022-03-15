package childrencare.app.repository;

import childrencare.app.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.UserModel;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
	
	public UserModel findByEmail(String email);
	
	@Query(value = "SELECT * FROM user_model WHERE username LIKE ?1",nativeQuery = true)
	public Page<UserModel> findUserByAttributes(String search,PageRequest pageRequest);

	@Modifying
	@Query(value = "UPDATE user_model SET role_id = ?1 WHERE username = ?2",nativeQuery = true)
	public void updateUserRole(Integer roleId,String username);

	@Modifying
	@Query(value = "Update user_model SET fullname = ?1, phone = ?2, gender = ?3 " +
			"WHERE email = ?4", nativeQuery = true )
	void updateInfo(String fullName, String mobile, boolean gender, String email);

	@Query(value = "Select * from user_model u inner join role r " +
			"on u.role_id = r.role_id where r.role_name = ?1", nativeQuery = true)
	public List<UserModel> findUserModelByUserRole(String roleName);

	@Query(value = "select *\n" +
			"from user_model u\n" +
			"inner join customer c on c.customer_email = u.email\n" +
			"inner join reservation r on r.customer_id = c.customer_id\n" +
			"where r.reservation_id = ?1", nativeQuery = true)
	public UserModel findUserModelByUserReservationId(int reserId);

	@Query(value = "SELECT * FROM user_model "
			+ "WHERE (status > ?2 AND status < ?3) AND (gender = ?4 OR ?4 = -1) AND (role_id = ?5 OR ?5 = -1) AND "
			+ "(phone LIKE ?1 OR fullname LIKE ?1 OR email LIKE ?1) ",
			countQuery = " SELECT COUNT(*) FROM user_model "
					+ "WHERE (status > ?2 AND status < ?3) AND (gender = ?4 OR ?4 = -1) AND (role_id = ?5 OR ?5 = -1) AND "
					+ "(phone LIKE ?1 OR fullname LIKE ?1 OR email LIKE ?1)",
			nativeQuery = true)
	public Page<UserModel> findUserByStatusAndSearchQuery(String search, int startBitRange, int endBitRange,int gender, int role_id,PageRequest pageRequest);

	@Modifying
	@Query(value ="Update user_model set status = ?1, role_id = ?2 where email = ?3" , nativeQuery = true)
	void updateStatusAndRole(int status, int role_id, String email);
}
