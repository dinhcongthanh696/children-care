package childrencare.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_history")
public class CustomerHistoryModel {
	
	@Column(name = "history_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int historyId;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private CustomerModel customer;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne
	@JoinColumn(name = "email")
	private UserModel updatedBy;

	private String fullname;
	private String customer_email;
	private boolean gender;
	private String mobile;
	private String address;
}
