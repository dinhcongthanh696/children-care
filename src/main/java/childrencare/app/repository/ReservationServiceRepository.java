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
public interface ReservationServiceRepository extends JpaRepository<ReservationServiceModel,ReservationModel> {




    @Query(value = "select * from\n" +
            "     reservation_service rs inner join\n" +
            "     reservation r on rs.reservation_id = r.reservation_id\n" +
            "     inner join customer c on c.customer_id = r.customer_id\n" +
            "     inner join user_model u on u.email = c.customer_email\n" +
            "     inner join [service] s on rs.service_id = s.service_id\n" +
            "     inner join slot sl on sl.slot_id = rs.slot_id\n" +
            "     inner join staff st on st.staff_id = rs.staff_id\n" +
            "     where c.customer_email = ?1 " , nativeQuery = true)
    List<ReservationServiceModel> findAllByEmail(String email);

    @Query(value = "select * from\n" +
            "                 reservation_service rs inner join\n" +
            "                reservation r on rs.reservation_id = r.reservation_id\n" +
            "                inner join customer c on c.customer_id = r.customer_id\n" +
            "                inner join user_model u on u.email = c.customer_email\n" +
            "                inner join [service] s on rs.service_id = s.service_id\n" +
            "                inner join slot sl on sl.slot_id = rs.slot_id\n" +
            "                inner join staff st on st.staff_id = rs.staff_id\n" +
            "                where CONCAT(r.reservation_id,u.fullname) like %?1%", nativeQuery = true)
     Page<ReservationServiceModel> findAll(String keyword, Pageable pageable);


    @Query(value = "select * from\n" +
            "     reservation_service rs inner join\n" +
            "     reservation r on rs.reservation_id = r.reservation_id\n" +
            "     inner join customer c on c.customer_id = r.customer_id\n" +
            "     inner join user_model u on u.email = c.customer_email\n" +
            "     inner join [service] s on rs.service_id = s.service_id\n" +
            "     inner join service_category sc on sc.service_category_id = s.service_id" +
            "     inner join slot sl on sl.slot_id = rs.slot_id\n" +
            "     inner join staff st on st.staff_id = rs.staff_id\n" +
            "     where  r.reservation_id = ?1" , nativeQuery = true)
    List<ReservationServiceModel> findAllByRid(int rid);



    @Query(value = "select * from reservation_service\n" +
            "where reservation_id = ?1 \n" +
            "order by service_id", nativeQuery = true)
    List<ReservationServiceModel> findAllBookedSchedule(int reservation_id);
}
