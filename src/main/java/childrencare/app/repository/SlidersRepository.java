package childrencare.app.repository;


import childrencare.app.model.SliderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface SlidersRepository extends PagingAndSortingRepository<SliderModel,Integer> {



    @Modifying
    @Query(value = "UPDATE [slide]\n" +
            "   SET [back_link] = ?1,\n" +
            "  [image] = ?2, [notes] = ?3,[status] = ?4,[title] = ?5\n" +
            " WHERE slide_id = ?6",nativeQuery = true)
    public void updateSlider(String back_link, byte[] img, String notes, boolean status, String title, int slide_id);


    @Modifying
    @Query(value = "INSERT INTO [slide]\n" +
            "([back_link],[image],[notes],[status] ,[title])\n" +
            "VALUES (?1,?2,?3,?4,?5)",nativeQuery = true)
    public void addSlider(String back_link, byte[] img, String notes, boolean status, String title);

    @Query(value = "select * from slide \n" +
            "where CONCAT(title,back_link) like %?1%",nativeQuery = true)
    public Page<SliderModel> findAllByField(String keyword, Pageable pageable);

    @Query(value = "select * from slide\n" +
            "            where [status] = ?1",nativeQuery = true)
    public Page<SliderModel> filterByStatus(int status, Pageable pageable);

}
