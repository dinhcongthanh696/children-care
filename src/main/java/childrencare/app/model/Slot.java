package childrencare.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "start_time")
    private double start;

    @Column(name = "end_time")
    private double end;

    @JsonIgnore
    @OneToMany(mappedBy = "slot")
    private List<ReservationServiceModel> reservationServices;
    
    public String getTimeToString(boolean isStart) {
    	double time = end;
    	if(isStart) time = start;
    	
    	int preComma = (int) Math.floor(time);
    	double sufComma = time % (preComma); 
    	
    	String hour = preComma + "h";
    	String minute = (sufComma == 0) ? "" : (int) ( sufComma * 60 ) + "p" ;
    	
    	return hour + minute;
    }


}
