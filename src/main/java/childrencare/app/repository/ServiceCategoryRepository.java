package childrencare.app.repository;

import java.util.Date;
import java.util.List;

import childrencare.app.model.ServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ServiceCategoryModel;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryModel, Integer>{
	
	@Query(value = "declare @check int\n" +
			"\n" +
			"select @check = SUM(rs.price) \n" +
			"\t\t\tfrom service_category as sc left join service as s on sc.service_category_id = s.service_category_id\n" +
			"\t\t\tleft join reservation_service as rs on s.service_id = rs.service_id\n" +
			"\t\t\tleft join reservation as r on rs.reservation_id = r.reservation_id\n" +
			"\t\t\tWHERE (MONTH(r.date) = ?1 AND YEAR(r.date) = ?2 OR r.date is null) AND sc.service_category_id = ?3\n" +
			"\t\t\tgroup by sc.service_category_id\n" +
			"\n" +
			"\t\t\tif @check is null \n" +
			"\t\t\tSET @check = 0\n" +
			"\n" +
			"\t\t\tselect @check ",nativeQuery = true)
	double getServiceRevenueByDate(Integer month,Integer year,Integer serviceCategoryId);

	@Query(value = "SELECT * FROM service_category WHERE service_category_name LIKE ?1", nativeQuery = true)
	ServiceCategoryModel getServiceCategoryModelByName(String name);

}
