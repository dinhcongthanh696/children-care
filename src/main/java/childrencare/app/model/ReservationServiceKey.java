package childrencare.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationServiceKey implements Serializable{
	private int service_id;
	private int reservation_id;
	private String username_doctor;
	private int slot_id;

}
