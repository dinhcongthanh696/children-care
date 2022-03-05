package childrencare.app.repository;


import childrencare.app.model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel,Integer> {

    @Query(value = "select status_reservation_Id from status_reservation\n" +
            "where status_reservation_id = ?1",nativeQuery = true)
    int findById(int id);



}
