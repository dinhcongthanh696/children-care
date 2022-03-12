package childrencare.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Slot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    @Id
    @Column(name = "slot_id")
    private int id;
    
    @Column(name = "start_time")
    private double start;

    @Column(name = "end_time")
    private double end;

    @JsonIgnore
    @OneToMany(mappedBy = "slot")
    private List<ReservationServiceModel> reservationServices;
    
    private String getTime() {
    	String startTime = start > 12 ? start + ":pm" : start + "am";
		String endTime = end > 12 ? end+ ":pm" : end + "am";
		return startTime + " -> "+endTime;
    }


}
