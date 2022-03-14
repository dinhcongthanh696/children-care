package childrencare.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query(value = "INSERT INTO [feedback]([comment],[created_date],[feedback_image],[rated_star],[status],[customer_id])VALUES (?1,?2,?3,?4,?5,?6) ",
            nativeQuery = true)
    public void saveGeneralFeedback(String comment,String date, byte[] image, Integer ratedStar,boolean status,  int custom_id);

    @Modifying
    @Query(value = "INSERT INTO [feedback]([comment],[created_date],[feedback_image],[rated_star],[status])VALUES (?1,?2,?3,?4,?5) ",
            nativeQuery = true)
    public void saveGeneralFeedback(String comment,String date, byte[] image, Integer ratedStar,boolean status);

    @Query(value = "SELECT * FROM feedback WHERE service_id = ?1 AND DATEDIFF(day,created_date,GETDATE()) <= ?2",nativeQuery = true)
    public List<FeedbackModel> findByServiceByLastDays(int serviceId,int numberOfDays); 
    
    public List<FeedbackModel> findByService(ServiceModel service);


    @Query(value = "select * from feedback where 1=1 " +
            "and (:sid = -1  or service_id = :sid) " +
            "and (:star = -1 or :star = 4) " +
            "and (:status = -1 or status = :status) " +
            "and (:content = '' or comment like %:content%) " +
            "and (:contactName = '' or customer_id in " +
            "(select customer_id from customer c " +
            "inner join user_model u on c.customer_email = u.email " +
            "where u.fullname like %:contactName%) ) ", nativeQuery = true)
    Page<FeedbackModel> getAllFeedBack(@Param("sid")int sid, @Param("star")int star,
                                       @Param("status")int status, @Param("content")String content,
                                       @Param("contactName") String contactName, Pageable pageable);

    @Query(value = "select * from feedback where 1=1 " +
            "and service_id is null " +
            "and (:star = -1 or :star = 4) " +
            "and (:status = -1 or status = :status) " +
            "and (:content = '' or comment like %:content%) " +
            "and (:contactName = '' or customer_id in " +
            "(select customer_id from customer c " +
            "inner join user_model u on c.customer_email = u.email " +
            "where u.fullname like %:contactName%) ) ", nativeQuery = true)
    Page<FeedbackModel> getAllGeneralFeedback(@Param("star")int star,
                                       @Param("status")int status, @Param("content")String content,
                                       @Param("contactName") String contactName, Pageable pageable);

    @Modifying
    @Query(value = "Update feedback set status = ?1 where feedback_id = ?2", nativeQuery = true)
    void changeFeedback(int status, int feedback_id);
}
