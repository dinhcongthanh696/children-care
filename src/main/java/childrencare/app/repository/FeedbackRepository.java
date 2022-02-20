package childrencare.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceModel;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer> {

    @Modifying
    @Query(value = "INSERT INTO [dbo].[feedback]\r\n"
    		+ "           ([comment]\r\n"
    		+ "           ,[created_date]\r\n"
    		+ "           ,[feedback_image]\r\n"
    		+ "           ,[rated_star]\r\n"
    		+ "           ,[status]\r\n"
    		+ "           ,[customer_id]\r\n"
    		+ "           ,[service_id]) VALUES (?1,GETDATE(),?2,?3,?4,?6,?5) ", nativeQuery = true)
    public void saveOnlyFeedback(String comment,byte[] image, Integer ratedStar, boolean status, Integer serviceId , Integer customerId);

    @Query(value = "INSERT INTO feedback([feedback_id],[fullname],[gender],[email],[phone],[feedback_image],[rated_star],[comment]) VALUES ((select next_val + 1 from feedback_id_sequence) ,?1,?2,?3,?4,?5,?6,?7) "
            + "UPDATE feedback_id_sequence SET next_val = next_val + 1", nativeQuery = true)
    public void saveGeneralFeedback(String fullname, boolean gender, String email, String phone, byte[] image, Integer ratedStar, String comment);

    @Query(value = "SELECT * FROM feedback WHERE service_id = ?1 AND DATEDIFF(day,created_date,GETDATE()) <= ?2",nativeQuery = true)
    public List<FeedbackModel> findByServiceByLastDays(int serviceId,int numberOfDays); 
    
    public List<FeedbackModel> findByService(ServiceModel service);

}
