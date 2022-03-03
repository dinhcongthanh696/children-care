package childrencare.app.repository;

import childrencare.app.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<PostModel, Integer> {

    @Query(value = "select * from post where post.title like ?1 order by post.updated_at desc", nativeQuery = true)

    public Page<PostModel> findAllByTitle(String title, PageRequest pageRequest);

    @Query(value = "select * from post where post.title like ?1 and post.post_category_id = ?2 order by post.updated_at desc", nativeQuery = true)
    public Page<PostModel> findAllByPostCategory(String title, int postCategoryId, PageRequest pageRequest);

    @Query(value = "select top 3 * from post order by updated_at desc", nativeQuery = true)
    public List<PostModel> findTop3RecentPost();

    //manager post

    @Query(value = "select * from post as p where 1=1 and (:title is null or p.title like %:title%) " +
            "and (:catID is null or p.post_category_id = :catID) " +
            "and (:type is null or p.status = :type)", nativeQuery = true)
    Page<PostModel> findAllBy(@Param("title")String title,@Param("catID")String catID,@Param("type")String type, Pageable pageable);

    @Query(value = "select * from post \n " +
            "(where post.post_category_id = ?1) ",nativeQuery = true)
    public Page<PostModel> findAllByPostCategory(int cateID,PageRequest pageRequest);

    @Query(value = "select * from post where post.status = ?1 ",nativeQuery = true)
    public Page<PostModel> findAllByPostStatus(int status,PageRequest pageRequest);
}
