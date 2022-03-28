package childrencare.app.model;

import java.util.Base64;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ServiceModel {

	public ServiceModel(int serviceId, String title){
		this.serviceId = serviceId;
		this.title = title;
	}

	public ServiceModel(String title, String briefInfo, double originalPrice,
						double salePrice, int quantity, String description, boolean status , ServiceCategoryModel serviceCategory) {
		this.title = title;
		this.briefInfo = briefInfo;
		this.originalPrice = originalPrice;
		this.salePrice = salePrice;
		this.quantity = quantity;
		this.description = description;
		this.status = status;
		this.serviceCategory = serviceCategory;
	}

	public ServiceModel(int serviceId, byte[] thumbnail, String title, String briefInfo, double originalPrice,
						double salePrice, int quantity, String description) {
		this.serviceId = serviceId;
		this.thumbnail = thumbnail;
		this.title = title;
		this.briefInfo = briefInfo;
		this.originalPrice = originalPrice;
		this.salePrice = salePrice;
		this.quantity = quantity;
		this.description = description;

	}
	
	public ServiceModel(byte[] thumbnail, String title, String briefInfo, double originalPrice, double salePrice,
			int quantity, String description, boolean status, ServiceCategoryModel serviceCategory) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.briefInfo = briefInfo;
		this.originalPrice = originalPrice;
		this.salePrice = salePrice;
		this.quantity = quantity;
		this.description = description;
		this.status = status;
		this.serviceCategory = serviceCategory;
	}
	
	public ServiceModel(int serviceId, String base64ThumbnailEncode, double originalPrice, int quantity) {
		super();
		this.serviceId = serviceId;
		this.base64ThumbnailEncode = base64ThumbnailEncode;
		this.originalPrice = originalPrice;
		this.quantity = quantity;
	}

	@Id
	@Column(name = "service_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int serviceId;
	private byte[] thumbnail;
	private String title;
	@Transient
	private double avg_star = -1;

	@Column(name = "brief_info")
	private String briefInfo;

	@Column(name = "original_price")
	private double originalPrice;

	@Column(name = "sale_price")
	private double salePrice;

	private int quantity;

	private String description;
	
	private boolean status;

	@Transient
	private String base64ThumbnailEncode; 

	@OneToMany(mappedBy = "service")
	@JsonIgnore
	private List<FeedbackModel> feedbacks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_category_id")
	@JsonIgnore
	private ServiceCategoryModel serviceCategory;
	
	@JsonIgnore
	@OneToMany(mappedBy = "service")
	private List<ReservationServiceModel> reservationServices;
	
	@OneToMany(mappedBy = "service")
	private List<ReservationServiceDrugModel> reservationServiceDrugs;
	
	public String toCookieValue() {
		return this.getServiceId()+"_"+this.getQuantity();
	}

	@Transient
	public double getTotalCost(){
		return quantity*salePrice;
	}
	
	public void setBase64ThumbnailEncode(byte[] thumbnail) {
		if(thumbnail != null) {
			this.base64ThumbnailEncode = Base64.getEncoder().encodeToString(thumbnail);
		}
	}



}
