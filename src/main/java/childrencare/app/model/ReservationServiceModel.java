package childrencare.app.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "Reservation_Service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReservationServiceModel {
	@EmbeddedId
	private ReservationServiceKey id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	@MapsId("reservation_id")
	private ReservationModel reservation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	@MapsId("service_id")
	private ServiceModel service;

	@ManyToOne
	@JoinColumn(name = "staff_id")
	@MapsId("staff_id")
	private StaffModel staff;
	
	@Transient
	public List<DrugModel> drugs;

	@ManyToOne
	@JoinColumn(name = "slot_id")
	@MapsId("slot_id")
	private Slot slot;

	private double price;

	@Override
	public String toString(){
		String start = slot.getStart() > 12 ? slot.getStart() + ":pm" : slot.getStart() + "am";
		String end = slot.getEnd() > 12 ? slot.getEnd()+ ":pm" : slot.getEnd() + "am";

		return "Booked Date: [" + id.getBookedDate() + "]   ==  " +
				"Time:  [" + start + " -> " + end  +   "]   ==  " +
				"Service:  [" + service.getTitle() +   "]   ==  " +
				"Staff: [" + staff.getStaff_user().getUsername() + "]";
	}

}
