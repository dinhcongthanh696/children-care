package childrencare.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceModel;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer>{
	
	@Modifying
	@Query(value = "INSERT INTO feedback VALUES ((select next_val + 1 from feedback_id_sequence) ,?1,?2,?3,?4,?5,?6) "
			+ "UPDATE feedback_id_sequence SET next_val = next_val + 1", nativeQuery=true)
	public void saveOnlyFeedback(Integer ratedStar , boolean status , Integer serviceId , String username , String comment , byte[] image);


	public List<FeedbackModel> findByService(ServiceModel service);
}
