package childrencare.app.repository;

import childrencare.app.model.PostModel;
import childrencare.app.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<PostModel, Integer> {

    @Query(value = "select * from post where post.title like ?1 and post.status = 1 order by post.updated_at desc", nativeQuery = true)
    public Page<PostModel> findAllByTitle(String title, PageRequest pageRequest);

    @Query(value = "select * from post where post.title like ?1 and post.post_category_id = ?2 and post.status = 1 order by post.updated_at desc", nativeQuery = true)
    public Page<PostModel> findAllByPostCategory(String title, int postCategoryId, PageRequest pageRequest);

    @Query(value = "select top 3 * from post order by updated_at desc", nativeQuery = true)
    public List<PostModel> findTop3RecentPost();

    //manager post

    @Query(value = "select * from post as p where 1=1 and (:title is null or p.title like %:title%) " +
            "and (:catID is null or p.post_category_id = :catID) " +
            "and (:type is null or p.status = :type)", nativeQuery = true)
    Page<PostModel> findAllBy(@Param("title")String title,@Param("catID")String catID,@Param("type")String type, Pageable pageable);

    @Query(value = "select u.email,u.fullname from user_model as u where u.role_id = 5", nativeQuery = true)
    List<UserModel> findManager();



    @Modifying
    @Query(value = "UPDATE post \n" +
            "   SET status = ?1\n" +
            " WHERE post_id = ?2",nativeQuery = true)
    void changeStatusPost(int status, int rid);



}
