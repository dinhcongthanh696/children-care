package childrencare.app.repository;

import childrencare.app.model.PostCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCategoryRepository extends JpaRepository<PostCategoryModel,Integer> {
}
