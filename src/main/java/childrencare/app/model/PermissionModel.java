package childrencare.app.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Permission")
public class PermissionModel {
	@EmbeddedId
	private PermissionKey id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	@MapsId("role_id")
	private RoleModel role;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id")
	@MapsId("screen_id")
	private ScreenModel screen;
	
	public String toString() {
		return "";
	}
}
