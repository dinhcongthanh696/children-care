package childrencare.app.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "service_category")
@Data
@RequiredArgsConstructor
public class ServiceCategoryModel {
	@Id
	@Column(name = "service_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int serviceCategoryId; 
	@Column(name = "service_category_name")
	private String serviceCategoryName;
	
	@Transient
	private double totalRevenue;
	
	@OneToMany(mappedBy = "serviceCategory")
	List<ServiceModel> services;
}
