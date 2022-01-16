package childrencare.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class PermissionKey implements Serializable{
	private int role_id;
	private int screen_id;
}
