package childrencare.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Post")
@Data
@AllArgsConstructor
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
	private byte[] thumbnail;
	private String title = "";
	@Column(name = "brief_info")
	private String briefInfo;
	private String details;
	@Column(name = "create_at")
	private Date createAt;
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email")
	private UserModel author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_category_id")
	private PostCategoryModel postCategory;

	public PostModel() {
	}
}
