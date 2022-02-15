package childrencare.app.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Screen")
public class ScreenModel {
	@Id
	@Column(name = "screen_id")
	private int screenId;
	@Column(name = "screen_name")
	private String screenName;
	private String url;
	
	@OneToMany(mappedBy = "screen")
	private List<PermissionModel> permissions;
	
	private String method;
}
