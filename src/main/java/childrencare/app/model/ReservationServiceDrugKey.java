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
public class ReservationServiceDrugKey implements Serializable{
	
	private int reservation_id;
	private int service_id;
	private int drug_id;
	
}
