package childrencare.app.repository;


import childrencare.app.model.SliderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface SlidersRepository extends JpaRepository<SliderModel,Integer> {

    @Modifying
    @Query(value = "update slide\n" +
            "set [status] = ?1 where slide_id = ?2",nativeQuery = true)
    public void updatestatusSlider(boolean status,int slide_id);

    @Modifying
    @Query(value = "UPDATE [slide]\n" +
            "   SET [back_link] = ?1,\n" +
            "  [image] = ?2, [notes] = ?3,[status] = ?4,[title] = ?5\n" +
            " WHERE slide_id = ?6",nativeQuery = true)
    public void updateSlider(String back_link, MultipartFile img, String notes, boolean status, String title, int slide_id);






}
