package childrencare.app.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackKey implements Serializable{
	private String username;
	private int service_id;
	
	@Override
	public int hashCode() {
		return Objects.hash(service_id, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedbackKey other = (FeedbackKey) obj;
		return Objects.equals(service_id, other.service_id) && Objects.equals(username, other.username);
	}
	
}
