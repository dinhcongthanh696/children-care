package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import childrencare.app.model.DrugModel;

@Repository
public interface DrugRepository extends JpaRepository<DrugModel, Integer>{
	
}
