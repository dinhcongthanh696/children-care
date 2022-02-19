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
@Table(name = "customer")
public class CustomerModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int customer_id;
	private int status;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
	private UserModel customer_user; 
	
	@OneToMany(mappedBy = "customer")
	private List<ReservationModel> reservations;
	
	@OneToMany(mappedBy = "customer")
	private List<FeedbackModel> feedbacks;
	
}
