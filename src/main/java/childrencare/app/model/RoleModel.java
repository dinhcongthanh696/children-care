package childrencare.app.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
	@Id
	@Column(name = "role_id")
	@SequenceGenerator(
			sequenceName = "role_id_sequence",
			name = "role_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "role_id_sequence"
	)
	private int roleId;
	@Column(name = "role_name")
	private String roleName;
	
	@JsonIgnore
	@OneToMany(mappedBy = "userRole")
	private List<UserModel> users; 
	
	@OneToMany(mappedBy = "role")
	private List<PermissionModel> permissions;
	
	public String toString() {
		return "";
	}
}
