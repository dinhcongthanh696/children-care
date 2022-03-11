package childrencare.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ReservationServiceDrugKey;
import childrencare.app.model.ReservationServiceDrugModel;

@Repository
public interface ReservationServiceDrugRepository extends JpaRepository<ReservationServiceDrugModel, ReservationServiceDrugKey>{

}
