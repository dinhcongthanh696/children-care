package childrencare.app.repository;


import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ScreenModel;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenModel, Integer>{
	
	@Query(value = "select * from screen where screen_name LIKE ?1" ,
			countQuery = "select count(*) from screen where screen_name LIKE ?1",
			nativeQuery = true)
	public Page<ScreenModel> findByNameContaining(String search,PageRequest pageRequest);
	
	public ScreenModel findByUrl(String url);
}
