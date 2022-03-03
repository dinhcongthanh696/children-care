package childrencare.app.repository;

import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.SliderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationServiceRepository extends JpaRepository<ReservationServiceModel,ReservationModel> {

    @Query(value = "select rs from ReservationServiceModel rs\n" +
            "join rs.reservation r join r.customer c  where c.customer_user.email =?1")
    Page<ReservationServiceModel> findCustomerByEmail(String email,Pageable pageable);

    @Query(value = "select rs from ReservationServiceModel rs\n" +
            "join rs.reservation r join r.customer c " +
            "join c.customer_user u where c.customer_user.fullname like %?1% OR rs.reservation.reservationId = ?1")
    Page<ReservationServiceModel> findAllReserInfo(String keyword,Pageable pageable);

    @Query(value = "select rs from ReservationServiceModel rs\n" +
            "join rs.reservation r join r.customer c " +
            "join c.customer_user u")
    Page<ReservationServiceModel> findAll(Pageable pageable);


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

    @Query(value = "select *" +
            "from reservation_service where booked_date between ?3 and ?2", nativeQuery = true)
    List<ReservationServiceModel> listDate(int reservation_id);


    @Query(value = "select rs from ReservationServiceModel rs\n" +
            "join rs.reservation r join r.customer c " +
            "join c.customer_user u where r.status = ?1")
    Page<ReservationServiceModel> filterReservationByStatus(boolean status, Pageable pageable);

    @Query(value = " select * from reservation_service  rs inner join [service] s\n" +
            " on s.service_id =rs.service_id where rs.staff_id = ?1 and rs.reservation_id = ?2", nativeQuery = true)
    List<ReservationServiceModel> findAllServiceByStaffAndRid(int staffID,int reservation_id);

    @Modifying
    @Query(value = "DELETE FROM reservation_service WHERE reservation_id = ?1 and service_id = ?2 and slot_id = ?3 and booked_date = ?4",nativeQuery =true)
     void deleteInListServiceByReservationAndServiceAndSlotid(int rid, int sid, int slotid, String date);


}
