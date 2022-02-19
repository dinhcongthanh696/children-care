package childrencare.app.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "staff")
public class StaffModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int staff_id;
	private int status;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_email", referencedColumnName = "email")
	private UserModel staff_user;
	
	@OneToMany(mappedBy = "staff")
	private List<ReservationServiceModel> reservationServices;
}
