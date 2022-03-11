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

import java.sql.Date;
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


    @Query(value = " select * from reservation_service  rs inner join [service] s\n" +
            " on s.service_id =rs.service_id where  rs.reservation_id = ?1", nativeQuery = true)
    List<ReservationServiceModel> findAllByRid(int reservation_id);


    @Query(value = "select * from reservation_service\n" +
            "where reservation_id = ?1 \n" +
            "order by service_id", nativeQuery = true)
    List<ReservationServiceModel> findAllBookedSchedule(int reservation_id);

    @Query(value = "select *" +
            "from reservation_service where booked_date between ?3 and ?2", nativeQuery = true)
    List<ReservationServiceModel> listDate(int reservation_id);


    @Query(value = "select rs from ReservationServiceModel rs\n" +
            "join rs.reservation r join r.customer c " +
            "join c.customer_user u where r.statusReservation.statusId = ?1")
    Page<ReservationServiceModel> filterReservationByStatus(int status, Pageable pageable);

    @Query(value = " select * from reservation_service  rs inner join [service] s\n" +
            " on s.service_id =rs.service_id where rs.staff_id = ?1 and rs.reservation_id = ?2", nativeQuery = true)
    List<ReservationServiceModel> findAllServiceByStaffAndRid(int staffID,int reservation_id);

    @Modifying
    @Query(value = "DELETE FROM reservation_service WHERE reservation_id = ?1 and service_id = ?2 and slot_id = ?3 and booked_date = ?4",nativeQuery =true)
     void deleteInListServiceByReservationAndServiceAndSlotid(int rid, int sid, int slotid, String date);

    @Modifying
    @Query(value = "UPDATE [reservation_service]\n" +
            "   SET  [staff_id] = ?1 where booked_date = ?2 and slot_id = ?3",nativeQuery =true)
    void assginOtherStaff(int staffID, Date booked_date, int slot_id);
    
    @Query(value = "select * from "
    		+ "reservation as r inner join reservation_service as rs "
    		+ "on r.reservation_id = rs.reservation_id inner join reservation_service_drug as rsd "
    		+ "on r.reservation_id = rs.reservation_id "
    		+ "WHERE rs.staff_id = ?1 OR rs.service_id = ?2 OR rsd.drug_id IN ?3 " , nativeQuery = true)
    Page<ReservationServiceModel> listReservationByStaffAndServiceAndDrugs(int staffId , int serviceId , Integer[] drugIds , Pageable pageable);








}
