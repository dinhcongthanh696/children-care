package childrencare.app.model;

import java.util.Base64;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel {
	@Id
	@Column(name = "feedback_id")
	@SequenceGenerator(
			sequenceName = "feedback_id_sequence",
			name = "feedback_id_sequence",
			allocationSize = 1
	)
	
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "feedback_id_sequence"
	)
	private int feedbackId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	@MapsId("service_id")
	@JsonIgnore
	private ServiceModel service;
	
	@Column(name = "rated_star")
	private double ratedStart;
	
	private String comment;
	
	@Column(name = "feedback_image")
	private byte[] image;
	
	@Transient
	private String base64ImageEncode;
	private boolean status;
	
	private String fullname;
	private boolean gender;
	private String email;
	private String phone;
	private String address;
	
	public void setBase64ImageEncode(byte[] image) {
		this.base64ImageEncode = Base64.getEncoder().encodeToString(image);
	}
	
}
