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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int service_id;
	private int reservation_id;
	private int staff_id;
	private int slot_id;

}
