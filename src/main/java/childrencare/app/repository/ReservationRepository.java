package childrencare.app.repository;

import childrencare.app.model.ReservationModel;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Integer> {

    @Modifying
    @Query(value = "UPDATE ReservationModel rm set rm.status = 'true' where rm.reservationId = ?1",
    nativeQuery = true)
    void changeStatus(int reservationId);
    
    @Modifying
    @Query(value = "insert into reservation_service (reservation_id,service_id,total_person)" +
            "values (?1, ?2, ?3)",nativeQuery = true)
    void insertReservation_Service(int rId,int sId, int total);


    @Query(value = "select top 1 * from reservation\r\n"
    		+ "inner join reservation_service\r\n"
    		+ "on reservation.reservation_id = reservation_service.reservation_id\r\n"
    		+ "where email = ?1 and service_id = ?2 " , nativeQuery = true)
    public ReservationModel getReservationByEmailAndServiceId(String email , Integer serviceId);


    @Modifying
    @Query(value = "insert into reservation_service " +
            "values (?1, ?2, ?3, ?4, ?5)",nativeQuery = true)
    void createSchedule(int reservationId,int serviceId, int slotId, String doctor, double price);
    
    @Query(value = "SELECT COUNT(*) FROM reservation WHERE status = ?1",nativeQuery = true)
    int countReservationByStatus(int status);
    
    @Query(value = "SELECT COUNT(*) FROM reservation WHERE status = ?1 AND DATEPART(month,date) = DATEPART(MONTH,?2)\r\n"
    		+ "AND DATEPART(YEAR,date) = DATEPART(YEAR,?2) AND DATEPART(DAY,date) = DATEPART(day,?2)",nativeQuery = true)
    int countReservationByStatusAndDate(int status,Date date);
    @Query(value = "select *\n" +
            "from reservation r\n" +
            "inner join reservation_service rc on rc.reservation_id = r.reservation_id\n" +
            "inner join slot on slot.slot_id = rc.slot_id\n" +
            "inner join [service] serv on serv.service_id= rc.service_id\n" +
            "where r.reservation_id = ?1 \n" +
            "",nativeQuery = true)
    public ReservationModel getReservationModelByReservationId(int reserID);
}
