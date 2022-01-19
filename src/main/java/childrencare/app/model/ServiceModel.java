package childrencare.app.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceModel {
	@Id
	@Column(name = "service_id")
	@SequenceGenerator(
			sequenceName = "service_id_sequence",
			name = "service_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "service_id_sequence"
	)
	private int serviceId;
	private byte[] thumbnail;
	private String title;

	public ServiceModel(int serviceId, byte[] thumbnail,
						String title, String briefInfo,
						double originalPrice, double salePrice, int quantity, String description) {
		this.serviceId = serviceId;
		this.thumbnail = thumbnail;
		this.title = title;
		this.briefInfo = briefInfo;
		this.originalPrice = originalPrice;
		this.salePrice = salePrice;
		this.quantity = quantity;
		this.description = description;
		this.feedbacks = feedbacks;
		this.serviceCategory = serviceCategory;
		this.reservationServices = reservationServices;
	}

	@Column(name = "brief_info")
	private String briefInfo;
	
	@Column(name = "original_price")
	private double originalPrice;
	
	@Column(name = "sale_price")
	private double salePrice;
	
	private int quantity;
	
	private String description;
	
	@OneToMany(mappedBy = "service")
	private List<FeedbackModel> feedbacks;
	
	@ManyToOne
	@JoinColumn(name = "service_category_id")
	private ServiceCategoryModel serviceCategory;
	
	@OneToMany(mappedBy = "service")
	private List<ReservationServiceModel> reservationServices;
}
