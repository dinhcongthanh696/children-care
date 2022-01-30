package childrencare.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "Slot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    @Id
    private int id;

    private Time start;
    private Time end;

    @OneToMany(mappedBy = "slot")
    private List<ReservationServiceModel> reservationServices;
}
