package childrencare.app.repository;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import childrencare.app.model.ServiceModel;

public interface ServiceRepository extends JpaRepository<ServiceModel, Integer>{
	 String serviceRatedDecendingQuery = "select TOP (?1) sv.service_id,sv.title,sv.brief_info,sv.description,sv.original_price,sv.quantity,sv.sale_price\r\n"
	 		+ ",AVG(feedback.rated_star) as stars,sv.service_category_id,sv.thumbnail\r\n"
	 		+ "from service as sv\r\n"
	 		+ "left join feedback on sv.service_id = feedback.service_id\r\n"
	 		+ "group by sv.service_id,sv.title,sv.brief_info,sv.description,sv.original_price,sv.quantity,sv.sale_price\r\n"
	 		+ ",sv.service_category_id,sv.thumbnail \r\n"
	 		+ "ORDER BY stars desc"; 
	 
	@Query(value = serviceRatedDecendingQuery , nativeQuery = true)
	public List<ServiceModel> findRatedServiceDescending(Integer number);
	
	 @Query(value = "SELECT * FROM service WHERE title LIKE ?1 ORDER BY title",
			    countQuery = "SELECT count(*) FROM service WHERE title = ?1",
			    nativeQuery = true)
	 public Page<ServiceModel> findByTitleLike(String title, PageRequest pageable);
	 
	 @Query(value = "SELECT * FROM service WHERE title LIKE ?1 AND service_category_id = ?2 ORDER BY title",
			    countQuery = "SELECT count(*) FROM service WHERE title = ?1 AND service_category_id = ?2",
			    nativeQuery = true)
	 public Page<ServiceModel> findByTitleLikeAndCategory(String title,int serviceCategoryId, PageRequest pageable);
	 
	 @Query(value = "SELECT * FROM service WHERE service_category_id = ?1 ORDER BY title",
			    countQuery = "SELECT count(*) FROM service WHERE service_category_id = ?1",
			    nativeQuery = true)
	 public Page<ServiceModel> findByTitleLikeAndCategory(int serviceCategoryId, PageRequest pageable);

	 //Update Quantity
	@Query(value = "UPDATE ServiceModel sm set sm.quantity = sm.quantity - ?1  where sm.serviceId = ?2",
			nativeQuery = true)
	void updateQuantity(int quantity, int serviceId);
}
