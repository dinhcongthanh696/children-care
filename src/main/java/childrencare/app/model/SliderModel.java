package childrencare.app.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Slide")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SliderModel {

	@Id
	@Column(name = "slide_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int slideId;
	private String title;
	private  byte[] image;
	@Column(name = "back_link")
	private String backLink;
	private boolean status;
	private String notes;

	@Transient
	private String base64ThumbnailEncode;

}
