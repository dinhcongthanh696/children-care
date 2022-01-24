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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	private Date date;
	@Column(name = "total_reservation_price")
	private double totalReservationPrice;
	
	private String fullname;
	private boolean gender;
	private String email;
	private String phone;
	private String address;
	private String notes;
	private boolean status;
	
	@OneToMany(mappedBy = "reservation")
	private List<ReservationServiceModel> reservationServices; 
}
