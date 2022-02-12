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
    @Query(value = "INSERT INTO feedback VALUES ((select next_val + 1 from feedback_id_sequence) ,?1,?2,?3,?4,?5,?6,?7,?8,?9,?10) "
            + "UPDATE feedback_id_sequence SET next_val = next_val + 1", nativeQuery = true)
    public void saveOnlyFeedback(String address, String comment, String email, String fullname, boolean gender,
                                 byte[] image, String phone, Integer ratedStar, boolean status, Integer serviceId);

    @Query(value = "INSERT INTO feedback([feedback_id],[fullname],[gender],[email],[phone],[feedback_image],[rated_star],[comment]) VALUES ((select next_val + 1 from feedback_id_sequence) ,?1,?2,?3,?4,?5,?6,?7) "
            + "UPDATE feedback_id_sequence SET next_val = next_val + 1", nativeQuery = true)
    public void saveGeneralFeedback(String fullname, boolean gender, String email, String phone, byte[] image, Integer ratedStar, String comment);


    public List<FeedbackModel> findByService(ServiceModel service);
}
