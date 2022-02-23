package childrencare.app.model;

import java.io.Serializable;
import java.sql.Date;

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
	private int staff_id;
	private int slot_id;
	private Date bookedDate;

}
