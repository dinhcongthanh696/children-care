package childrencare.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.DrugModel;

@Repository
public interface DrugRepository extends JpaRepository<DrugModel, Integer>{
	
	
	@Query(value = "SELECT * FROM drug as d inner join reservation_service_drug as rsd\r\n"
			+ "on d.drug_id = rsd.drug_id WHERE rsd.reservation_id = ?1 AND rsd.service_id = ?2" , nativeQuery = true)
	public List<DrugModel> findDrugByReservationAndService(int reservationId , int serviceId);
}
