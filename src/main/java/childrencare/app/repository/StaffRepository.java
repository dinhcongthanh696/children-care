package childrencare.app.repository;

import childrencare.app.model.StaffModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface StaffRepository extends JpaRepository<StaffModel, Integer> {

    @Query(value = "Select top 1 staff_id from staff \n" +
            "where staff_id not in \n" +
            "(Select staff_id from reservation_service\n" +
            "where booked_date = ?1 and slot_id = ?2)", nativeQuery = true)
    int getStaffBySlotAndDate(Date date, int staff_id);

    @Query(value = "select * from staff where staff_email = ?1", nativeQuery = true)
    StaffModel findStaffByEmail(String email);
}
