package childrencare.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.PermissionKey;
import childrencare.app.model.PermissionModel;
import childrencare.app.model.RoleModel;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionModel, PermissionKey>{
	@Modifying
	@Query(value = "INSERT INTO permission VALUES (?1,?2)",nativeQuery = true)
	public void addPermission(int roleId,int screenId);
	
	@Modifying
	@Query(value = "DELETE FROM permission WHERE role_id = ?1 AND screen_id IN ?2",nativeQuery = true)
	public void removeRoleScreens(int roleId,List<Integer> screenIds);
	
	public List<PermissionModel> findByRole(RoleModel roleModel);
}
