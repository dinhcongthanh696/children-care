package childrencare.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Slide")
@AllArgsConstructor
@Data
public class SliderModel {
	@Id
	@Column(name = "slide_id")
	@SequenceGenerator(
			sequenceName = "slide_id_sequence",
			name = "slide_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "slide_id_sequence"
	)
	private int slideId;
	private String title;
	private final byte[] image;
	@Column(name = "back_link")
	private String backLink;
	private boolean status;
	private String notes;
	
}
