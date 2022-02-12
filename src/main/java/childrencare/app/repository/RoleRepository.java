package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Integer>{
}
