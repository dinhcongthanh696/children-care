package childrencare.app.model;

import javax.persistence.Transient;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "drug")
public class DrugModel {
	@Id
	@Column(name = "drug_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int drugId;
	
	@Column(name = "drug_name")
	private String drugName;
	
	private String type;
	private byte[] thumbnail;
	
	@Transient
	private String base64ThumbnailEncode;
	private boolean status;
	private double price;
	@Column(name = "create_date")
	private Date createdDate;
	@Column(name = "end_date")
	private Date endDate;
	
	@OneToMany(mappedBy = "drug")
	private List<ReservationServiceDrugModel> reservationServiceDrugs;
}
