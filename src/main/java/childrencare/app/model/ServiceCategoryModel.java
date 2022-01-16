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

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "service_category")
@Data
@RequiredArgsConstructor
public class ServiceCategoryModel {
	@Id
	@Column(name = "service_category_id")
	@SequenceGenerator(
			sequenceName = "service_category_id_sequence",
			name = "service_category_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "service_category_id_sequence"
	)
	private int serviceCategoryId; 
	@Column(name = "service_category_name")
	private final String serviceCategoryName = "";
	
	@OneToMany(mappedBy = "serviceCategory")
	List<ServiceModel> services;
}
