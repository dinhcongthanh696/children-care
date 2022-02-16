package childrencare.app.model;

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
    private Date start;

    @Column(name = "end_time")
    private Date end;

    @OneToMany(mappedBy = "slot")
    private List<ReservationServiceModel> reservationServices;


}
