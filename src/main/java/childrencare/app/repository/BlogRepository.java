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

import java.util.Date;
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
    Page<PostModel> findAllBy(@Param("title") String title, @Param("catID") String catID, @Param("type") String type, Pageable pageable);

    @Query(value = "select max(p.post_id) from post as p", nativeQuery = true)
    int maxPostID();

    @Query(value = "select * from post as p where p.post_id = ?1", nativeQuery = true)
    PostModel getPostModelByPostId(int pid);


    @Modifying
    @Query(value = "UPDATE post \n" +
            "   SET status = ?1\n" +
            " WHERE post_id = ?2", nativeQuery = true)
    void changeStatusPost(int status, int rid);

    @Modifying
    @Query(value = "INSERT INTO [post] ([post_id],[brief_info],[create_at],[details],[thumbnail],[title],[updated_at],[email],[post_category_id],[status])\n" +
            "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    public void addPost(int postid, String brefinfo, Date create, String detail, byte[] img, String title, Date updateAt, String author, int category, boolean status);

    @Modifying
    @Query(value = "UPDATE [post] SET \n" +
            "      [brief_info] = ?1\n" +
            "      ,[details] = ?2\n" +
            "      ,[thumbnail] = ?3\n" +
            "      ,[title] = ?4\n" +
            "      ,[updated_at] = ?5\n" +
            "      ,[email] = ?6\n" +
            "      ,[post_category_id] = ?7\n" +
            "      ,[status] = ?8\n" +
            " WHERE [post_id] = ?9", nativeQuery = true)
    void upDatePost(String brefinfo, String detail, byte[] img, String title, Date updateAt, String author, int category, boolean status, int postId);

}
