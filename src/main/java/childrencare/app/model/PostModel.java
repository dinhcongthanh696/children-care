package childrencare.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Post")
@Data
@RequiredArgsConstructor
public class PostModel {
	@Id
	@Column(name = "post_id")
	@SequenceGenerator(
			sequenceName = "post_id_sequence",
			name = "post_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "post_id_sequence"
	)
	private int postId;
	@Column(nullable = true)
	private final byte[] thumbnail = null;
	private final String title = "";
	@Column(name = "brief_info")
	private final String briefInfo = "";
	private final String details = "";
	@Column(name = "create_at")
	private final Date createAt = null;
	@Column(name = "updated_at")
	private final Date updatedAt = null;
	
	@ManyToOne
	@JoinColumn(name = "username")
	private UserModel author;
	
	@ManyToOne
	@JoinColumn(name = "post_category_id")
	private PostCategoryModel postCategory;
	
}
