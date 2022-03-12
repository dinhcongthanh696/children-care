package childrencare.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ReservationServiceDrugKey;
import childrencare.app.model.ReservationServiceDrugModel;

@Repository
public interface ReservationServiceDrugRepository extends JpaRepository<ReservationServiceDrugModel, ReservationServiceDrugKey>{
	
	@Query(value = "select * from reservation_service_drug where reservation_id = ?1 AND service_id = ?2",nativeQuery = true)
	public List<ReservationServiceDrugModel> findByReservationAndService(int rid,int sid);
	
	@Modifying
	@Query(value = "DELETE FROM reservation_service_drug WHERE reservation_id = ?1 AND service_id = ?2" , nativeQuery = true)
	public void deleteByReservationAndService(int reservationId,int serviceId);
	
	@Modifying
	@Query(value = "INSERT INTO reservation_service_drug VALUES(?1,?2,?3,?4,?5)",nativeQuery = true)
	public void save(int did,int rid,int sid,String notes,int quantity);
}
