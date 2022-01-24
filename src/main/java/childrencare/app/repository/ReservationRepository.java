package childrencare.app.repository;

import childrencare.app.model.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Integer> {

    @Query(value = "UPDATE ReservationModel rm SET rm.status = 'true' WHERE rm.reservationId = ?1",
            nativeQuery = true)
    void changeStatus(int reservationId);
}
