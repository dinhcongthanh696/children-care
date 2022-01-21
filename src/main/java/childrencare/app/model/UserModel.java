package childrencare.app.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserModel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
	@Id
	private String username;
	
	private String password;
	
	private String fullname;
	
	private String phone;
	
	private boolean gender;
	
	private String email;
	
	private String address;
	
	private String notes;
	
	private boolean status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleModel userRole;
	
	@OneToMany(mappedBy = "author")
	private List<PostModel> userPosts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "username")
	private List<FeedbackModel> feedbacks;
	
}
