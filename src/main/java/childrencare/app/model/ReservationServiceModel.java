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



	private double price;

//	@Transient
//	public double totalCost(){
//		return service.getOriginalPrice()* totalPerson;
//	}
}
