package childrencare.app.repository;

import childrencare.app.model.ReservationModel;

import java.util.Date;
import java.util.List;

import childrencare.app.model.ReservationServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Integer> {


    // AKV - ReservationComplete
    @Modifying
    @Query(value = "UPDATE reservation set status_reservation_id = 1 where reservation_id = ?1",
    nativeQuery = true)
    void changeStatus(int reservationId);

    @Modifying
    @Query(value = "insert into reservation_service(reservation_id, service_id, slot_id, staff_id, booked_date, price) " +
            "values (?1, ?2, ?3, ?4, ?5, ?6)",nativeQuery = true)
    void createSchedule(int reservationId,int serviceId, int slot_id,
                        int staff_id, Date date, double price);

    @Modifying
    @Query(value = "Delete reservation_service " +
            " where slot_id = ?1 and staff_id = ?2 and booked_date = ?3",nativeQuery = true)
    void deleteSchedule(int slot_id, int staff_id, Date date);




    @Query(value = "select top 1 * from reservation\r\n"
    		+ "inner join reservation_service\r\n"
    		+ "on reservation.reservation_id = reservation_service.reservation_id\r\n"
    		+ "inner join customer on reservation.customer_id = customer.customer_id\n"
    		+ "where customer_email = ?1 and service_id = ?2 " , nativeQuery = true)
     ReservationModel getReservationByEmailAndServiceId(String email , Integer serviceId);

    
    @Query(value = "SELECT COUNT(*) FROM reservation WHERE status = ?1",nativeQuery = true)
    int countReservationByStatus(int status);
    
    @Query(value = "SELECT COUNT(*) FROM reservation inner join status_reservation on reservation.status_reservation_id = status_reservation.status_reservation_id\r\n"
    		+ "WHERE status_reservation.status_name = ?1 AND DATEPART(month,date) = DATEPART(MONTH,?2)\r\n"
    		+ "AND DATEPART(YEAR,date) = DATEPART(YEAR,?2) AND DATEPART(DAY,date) = DATEPART(day,?2)",nativeQuery = true)
    int countReservationByStatusAndDate(String statusName,Date date);


    @Query(value = "select r.reservation_id,r.date,r.status,r.notes,r.total_reservation_price,r.customer_id\n" +
            "from reservation r\n" +
            "inner join reservation_service rc on rc.reservation_id = r.reservation_id\n" +
            "inner join slot on slot.slot_id = rc.slot_id\n" +
            "inner join [service] serv on serv.service_id= rc.service_id\n" +
            "where r.reservation_id = ?1 \n" +
            "group by r.reservation_id,r.date,r.status,r.notes, r.total_reservation_price,r.customer_id",nativeQuery = true)
     ReservationModel getReservationModelByReservationId(int reserID);

    @Query(value = "select * from reservation where reservation_id = ?1",nativeQuery = true)
     ReservationModel getReservationByReservationId(int reserID);

    @Query(value = " select * from \n" +
            " reservation r inner join customer c \n" +
            "  on r.customer_id = c.customer_id \n" +
            "  inner join user_model u on u.email =c.customer_email\n" +
            "  where r.reservation_id = ?1",nativeQuery = true)
     ReservationModel getreservationDetail(int reserID);

    @Query(value = "select r from ReservationModel r join r.statusReservation rt " +
            "join r.customer c join c.customer_user u")
    Page<ReservationModel> findAll(Pageable pageable);

    @Query(value = "select r from ReservationModel  r join r.statusReservation rt join r.customer c " +
            "join c.customer_user u where  r.reservationId = ?1 or rt.statusId = ?2")
    Page<ReservationModel> findReservationStaff(int keyword,int statusID, Pageable pageable);


    @Query(value = "select r from ReservationModel r join r.statusReservation rt " +
            "join r.customer c " +
            "join c.customer_user u where rt.statusId = ?1 ")
    Page<ReservationModel> filterReservationByStatus1(int status, Pageable pageable);


    @Modifying
    @Query(value = "UPDATE [reservation]\n" +
            "   SET[status_reservation_id] =  ?1    \n" +
            " WHERE reservation_id =?2",nativeQuery = true)
    void changeStatusReservation(int status, int rid);


    @Query(value = "select * from reservation where reservation_id in\n" +
            "(select distinct reservation_id\n" +
            " from reservation_service\n" +
            " where staff_id = ?1 )",nativeQuery = true)
    Page<ReservationModel> listReservationByStaffID(int staffID,Pageable pageable);
    
    @Query(value = "select * from reservation where reservation_id in\n" +
            "(select distinct reservation_id\n" +
            " from reservation_service\n" +
            " where staff_id = ?1 and booked_date between ?2 and ?3)",nativeQuery = true)
    Page<ReservationModel> listReservationByDate(int staffID, Date dateFrom, Date dateTo,Pageable pageable);


    @Query(value = "select * from reservation where reservation_id in\n" +
            "(select distinct reservation_id\n" +
            " from reservation_service\n" +
            " where staff_id = ?1 and (reservation_id = ?2 or [status_reservation_id] = ?3) )",nativeQuery = true)
    Page<ReservationModel> listReservationByStaff(int staffID,int keyword,int statusID,Pageable pageable);


    @Query(value = "select * from reservation where reservation_id in\n" +
            "(select distinct reservation_id\n" +
            " from reservation_service\n" +
            " where staff_id = ?1 and [status_reservation_id] = ?2)",nativeQuery = true)
    Page<ReservationModel> listReservationByStaffByFilter(int staffID,int status,Pageable pageable);

    @Query(value = "select * from reservation where reservation_id in\n" +
            "(select distinct reservation_id\n" +
            " from reservation_service\n" +
            " where customer_id = ?1)",nativeQuery = true)
    Page<ReservationModel> listReservationByCusID(int cusID,Pageable pageable);

    @Query(value = "select * from reservation where customer_id = ?1",nativeQuery = true)
    public List<ReservationModel> listReservationByCusId(int customerId);
    
    @Modifying
    @Query(value = "UPDATE reservation SET total_reservation_price = (SELECT SUM(price) FROM reservation_service WHERE reservation_id = ?1) "
    		+ "WHERE reservation_id = ?1",nativeQuery = true)
    public void updateReservationTotalPrice(int reservationId);


    @Query(value = "select distinct top 14[date] from reservation order by [date] desc",nativeQuery = true)
    List<String> lastSevenDateReservation();

    @Query(value = "select sum(total_reservation_price)\n" +
            "from reservation where [date] = ?1 ",nativeQuery = true)
    int totalPricelastSevenDateReservation(String date);






}
