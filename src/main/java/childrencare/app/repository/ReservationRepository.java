package childrencare.app.repository;

import childrencare.app.model.ReservationModel;
import childrencare.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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


    @Query(value = "select * from slot \n" +
            "where slot_id not in \n" +
            "(select s.slot_id from slot s\n" +
            "inner join reservation_service rs on rs.slot_id = s.slot_id\n" +
            "group by s.slot_id)", nativeQuery = true)
    List<UserModel> getAvailableDoctor(Date date, String doctor);
}
