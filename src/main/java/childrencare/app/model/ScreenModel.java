package childrencare.app.model;

import java.util.List;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int screenId;
	@Column(name = "screen_name")
	private String screenName;
	private String url;
	
	@OneToMany(mappedBy = "screen")
	private List<PermissionModel> permissions;
	
	private String method;
}
