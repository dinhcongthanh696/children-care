package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ServiceCategoryModel;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryModel, Integer>{

}
