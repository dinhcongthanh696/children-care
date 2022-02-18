package childrencare.app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ServiceCategoryModel;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryModel, Integer>{
	
	@Query(value = "select ISNULL(SUM(rs.price),0) \r\n"
			+ "from service_category as sc left join service as s on sc.service_category_id = s.service_category_id\r\n"
			+ "left join reservation_service as rs on s.service_id = rs.service_id\r\n"
			+ "left join reservation as r on rs.reservation_id = r.reservation_id\r\n"
			+ "WHERE (MONTH(r.date) = ?1 AND YEAR(r.date) = ?2 OR r.date is null) AND sc.service_category_id = ?3\r\n"
			+ "group by sc.service_category_id,sc.service_category_name ",nativeQuery = true)
	double getServiceRevenueByDate(Integer month,Integer year,Integer serviceCategoryId);
}
