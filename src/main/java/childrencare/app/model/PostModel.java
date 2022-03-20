package childrencare.app.model;

import java.util.Date;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Column(name = "status")
	private boolean status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email")
	private UserModel author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_category_id")
	private PostCategoryModel postCategory;

	public PostModel() {
	}
	@Transient
	private String base64ThumbnailEncode;
}
