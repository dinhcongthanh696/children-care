package childrencare.app.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "Reservation_Service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationServiceModel {
	@EmbeddedId
	private ReservationServiceKey id;
	
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	@MapsId("reservation_id")
	private ReservationModel reservation;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	@MapsId("service_id")
	private ServiceModel service;

	@ManyToOne
	@JoinColumn(name = "staff_id")
	@MapsId("staff_id")
	private Staff staff;

	@ManyToOne
	@JoinColumn(name = "slot_id")
	@MapsId("slot_id")
	private Slot slot;
	
	@Column(name = "total_person")
	private int totalPerson;

	@Transient
	public double totalCost(){
		return service.getOriginalPrice()* totalPerson;
	}


	private double price;

}
