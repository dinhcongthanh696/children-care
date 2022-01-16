package childrencare.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class ReservationServiceKey implements Serializable{
	private int service_id;
	private int reservation_id;
}
