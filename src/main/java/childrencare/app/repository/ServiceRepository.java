package childrencare.app.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.ServiceModel;

import javax.transaction.Transactional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Integer> {
    String serviceRatedDecendingQuery = "select TOP (?1) sv.service_id,sv.title,sv.brief_info,sv.description,sv.original_price,sv.quantity,sv.sale_price,sv.status\r\n"
            + ",AVG(feedback.rated_star) as stars,sv.service_category_id,sv.thumbnail\r\n"
            + "from service as sv\r\n"
            + "left join feedback on sv.service_id = feedback.service_id\r\n"
            + "group by sv.service_id,sv.title,sv.brief_info,sv.description,sv.original_price,sv.quantity,sv.sale_price,sv.status\r\n"
            + ",sv.service_category_id,sv.thumbnail \r\n"
            + "ORDER BY stars desc";

    @Query(value = serviceRatedDecendingQuery, nativeQuery = true)
    public List<ServiceModel> findRatedServiceDescending(Integer number);

    @Query(value = "SELECT * FROM service WHERE title LIKE ?1 OR brief_info LIKE ?1",
            countQuery = "SELECT count(*) FROM service WHERE title LIKE ?1 OR brief_info LIKE ?1",
            nativeQuery = true)
    public Page<ServiceModel> findByTitleOrBriefInfoLike(String search, PageRequest pageable);

    @Query(value = "SELECT * FROM service WHERE (title LIKE ?1 OR brief_info LIKE ?1) AND service_category_id = ?2 ORDER BY title,service_category_id",
            countQuery = "SELECT count(*) FROM service WHERE (title LIKE ?1 OR brief_info LIKE ?1) AND service_category_id = ?2",
            nativeQuery = true)
    public Page<ServiceModel> findByTitleLikeAndCategory(String search, int serviceCategoryId, PageRequest pageable);
    
    
    @Query(value = "SELECT * FROM service WHERE (title LIKE ?1 OR brief_info LIKE ?1) AND ([status] > ?2 AND [status] < ?3) ",
            countQuery = "SELECT count(*) FROM service WHERE (title LIKE ?1 OR brief_info LIKE ?1) AND ([status] > ?2 AND [status] < ?3)",
            nativeQuery = true)
    public Page<ServiceModel> findByTitleOrBriefInfoLikeAndStatus(String search, int start , int end, PageRequest pageable);
    //Update Quantity

    @Modifying
    @Query(value = "UPDATE service set quantity = quantity - ?1  where service_id = ?2",
            nativeQuery = true)
    void updateQuantity(int quantity, int serviceId);
    
    @Modifying
    @Query(value = "UPDATE service set status = ?1 where service_id = ?2" , nativeQuery = true)
    void updateStatus(boolean status,Integer serviceId);


    @Query(value = "select sv.brief_info,sv.description,sv.original_price,sv.quantity,sv.sale_price,sv.service_id,sv.thumbnail,sv.title,sv.service_category_id\r\n"
            + "from reservation as rv inner join reservation_service as rssv\r\n"
            + "ON rv.reservation_id = rssv.reservation_id\r\n"
            + "inner join [service] as sv on rssv.service_id = sv.service_id \r\n"
            + "WHERE rv.email = ?1 AND sv.service_id = ?2  "
            + "", nativeQuery = true)
    public List<ServiceModel> findByUserEmailAndServiceId(String email, Integer serviceId);

    @Query(value = "select * from service serv\n" +
            "inner join reservation_service rc on rc.service_id = serv.service_id\n" +
            "where rc.reservation_id = ?1", nativeQuery = true)
    public List<ServiceModel> findListServiceByReservationID(int reserId);
}
