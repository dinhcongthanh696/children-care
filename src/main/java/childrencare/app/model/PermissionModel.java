package childrencare.app.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "Permission")
public class PermissionModel {
	@EmbeddedId
	private PermissionKey id;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	@MapsId("role_id")
	private RoleModel role;
	
	@ManyToOne
	@JoinColumn(name = "screen_id")
	@MapsId("screen_id")
	private ScreenModel screen;
}
