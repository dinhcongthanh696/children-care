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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "post_category")
@AllArgsConstructor
@NoArgsConstructor
public class PostCategoryModel {
	@Id
	@SequenceGenerator(
			sequenceName = "post_category_id_sequence",
			name = "post_category_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "post_category_id_sequence"
	)
	@Column(name = "post_category_id")
	private int postCategoryId;
	@Column(name = "post_category_name")
	private String postCategoryName; 
	
	@OneToMany(mappedBy = "postCategory")
	private List<PostModel> posts;

}
