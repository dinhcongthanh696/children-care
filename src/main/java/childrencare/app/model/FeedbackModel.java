package childrencare.app.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel {
	@EmbeddedId
	private FeedbackKey id;
	
	@ManyToOne
	@JoinColumn(name = "username")
	@MapsId("username")
	private UserModel username;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	@MapsId("service_id")
	private ServiceModel service;
	
	@Column(name = "rated_star")
	private double ratedStart;
	
	private boolean status;
}
