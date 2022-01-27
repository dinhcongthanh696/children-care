package childrencare.app.repository;

import childrencare.app.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<PostModel, Integer> {

    @Query(value = "select * from post where post.title like ?1 order by post.updated_at desc", nativeQuery = true)

    public Page<PostModel> findAllByTitle(String title, PageRequest pageRequest);

    @Query(value = "select * from post where post.title like ?1 and post.post_category_id = ?2 order by post.updated_at desc", nativeQuery = true)
    public Page<PostModel> findAllByPostCategory(String title, int postCategoryId, PageRequest pageRequest);
}
