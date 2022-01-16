package childrencare.app.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Reservation")
@Data
@RequiredArgsConstructor
public class ReservationModel {
	@Id
	@Column(name = "reservation_id")
	@SequenceGenerator(
			sequenceName = "reservation_id_sequence",
			name = "reservation_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "reservation_id_sequence"
	)
	private int reservationId;
	private final Date date = null;
	@Column(name = "total_reservation_price")
	private final double totalReservationPrice = 0;
	
	private final String fullname = "";
	private final boolean gender = false;
	private final String email = "";
	private final String phone = "";
	private final String address = "";
	private final String notes = "";
	private final boolean status = false;
	
	@OneToMany(mappedBy = "reservation")
	private List<ReservationServiceModel> reservationServices; 
}
