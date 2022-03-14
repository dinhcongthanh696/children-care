package childrencare.app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import childrencare.app.model.DrugModel;

@Repository
public interface DrugRepository extends JpaRepository<DrugModel, Integer> {


    @Query(value = "SELECT * FROM drug as d inner join reservation_service_drug as rsd\r\n"
            + "on d.drug_id = rsd.drug_id WHERE rsd.reservation_id = ?1 AND rsd.service_id = ?2", nativeQuery = true)
    public List<DrugModel> findDrugByReservationAndService(int reservationId, int serviceId);

    @Modifying
    @Query(value = "UPDATE drug SET quantity = ?2 WHERE drug_id = ?1", nativeQuery = true)
    public void updateDrugQuantity(int drugId, int quantity);
    
    @Query(value = "SELECT * FROM drug WHERE status = ?1",nativeQuery = true)
    public List<DrugModel> findDrugByStatus(boolean status);

    @Modifying
    @Query(value = "INSERT INTO [drug]([create_date],[drug_name],[end_date],[price],[status],[thumbnail],[type],[quantity]) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    public void addDrug(Date createDate, String drugname, Date endDate, int price, boolean status, byte[] image, String type, int quantity);

    @Modifying
    @Query(value = "UPDATE [drug]\n" +
            "   SET [create_date] = ?1\n" +
            "      ,[drug_name] = ?2\n" +
            "      ,[end_date] = ?3\n" +
            "      ,[price] = ?4\n" +
            "      ,[thumbnail] = ?5\n" +
            "      ,[type] = ?6\n" +
            "      ,[quantity] = ?7\n" +
            " WHERE drug_id = ?8", nativeQuery = true)
    public void updateDrug(Date createDate, String drugname, Date endDate, float price, byte[] image, String type, int quantity, int drug_id);

    @Query(value = "select * from drug as d where 1=1 and (:title is null or d.drug_name like %:title%)",nativeQuery = true)
    public Page<DrugModel> findAllBy(@Param("title") String title, Pageable pageable);
}
