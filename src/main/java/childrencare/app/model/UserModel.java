package childrencare.app.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserModel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {




	public UserModel(String username, String password, String fullname, String phone, String email, String address) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.phone = phone;
		this.email = email;
		this.address = address;
	}

	@Column(unique = true)
	private String username;

	@JsonIgnore
	private String password;

	private String fullname;

	private String phone;

	private boolean gender;
	
	@Id
	private String email;

	private String address;

	private String notes;

	private boolean status;
	
	@Column(name = "register_date")
	private Date regiteredDate;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleModel userRole;

	@JsonIgnore
	@OneToMany(mappedBy = "author")
	private List<PostModel> userPosts;
	
	public String toString() {
		return "User : "+username+" Role : "+userRole.getRoleName();
	}
	
	@OneToOne(mappedBy = "customer_user",fetch = FetchType.LAZY)
	private CustomerModel customer;
	
	@OneToOne(mappedBy = "staff_user")
	private StaffModel staff;

}
