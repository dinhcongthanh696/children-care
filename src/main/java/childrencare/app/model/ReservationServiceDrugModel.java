package childrencare.app.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "reservation_service_drug")
public class ReservationServiceDrugModel {
	
	@EmbeddedId
	private ReservationServiceDrugKey embededId;
	
	private int quantity;
	private String notes;
	
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	@MapsId("reservation_id")
	private ReservationModel reservation;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	@MapsId("service_id")
	private ServiceModel service;
	
	@ManyToOne
	@JoinColumn(name = "drug_id")
	@MapsId("drug_id")
	private DrugModel drug;
}
