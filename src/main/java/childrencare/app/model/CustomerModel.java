package childrencare.app.model;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class CustomerModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int customer_id;
	private int status;

	@Transient
	private String base64AvatarEncode;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
	private UserModel customer_user; 
	
	@OneToMany(mappedBy = "customer")
	private List<ReservationModel> reservations;
	
	@OneToMany(mappedBy = "customer")
	private List<FeedbackModel> feedbacks;

	
	
}
