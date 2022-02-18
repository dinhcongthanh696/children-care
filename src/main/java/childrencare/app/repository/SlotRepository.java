package childrencare.app.repository;

import childrencare.app.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {

    @Query(value = "select * from slot \n" +
            "where slot_id not in \n" +
            "(select s.slot_id from slot s\n" +
            "inner join reservation_service rs on rs.slot_id = s.slot_id\n" +
            "where rs.bookedDate = ?1\n" +
            "group by s.slot_id\n" +
            "having count(*) < (select count(*) from user_model))", nativeQuery = true)
    List<Slot> getAvailableSlot(Date date);

    @Query(value = "select slot.slot_id,slot.start_time,slot.end_time\n" +
            "from slot\n" +
            "inner join reservation_service rc on rc.slot_id = slot.slot_id\n" +
            "inner join reservation r on r.reservation_id = rc.reservation_id\n" +
            "where r.reservation_id = ?1\n" +
            "group by slot.slot_id,slot.start_time,slot.end_time", nativeQuery = true)
    public Slot getSlotByReservationID(int reserID);


}
