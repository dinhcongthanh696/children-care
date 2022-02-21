package childrencare.app.repository;

import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationServiceRepository extends JpaRepository<ReservationServiceModel,Integer> {




    @Query(value = "select * from\n" +
            "reservation_service rs inner join\n" +
            "reservation r on rs.reservation_id = r.reservation_id\n" +
            "inner join [service] s on rs.service_id = s.service_id\n" +
            "inner join slot sl on sl.slot_id = rs.slot_id\n" +
            "inner join user_model us on us.username =rs.username_doctor\n" +
            "where r.email = ?1" , nativeQuery = true)
    List<ReservationServiceModel> findAllByEmail(String email);

    @Query(value = "select * from reservation_service\n" +
            "where reservation_id = ?1 \n" +
            "order by service_id", nativeQuery = true)
    List<ReservationServiceModel> findAllBookedSchedule(int reservation_id);
}
