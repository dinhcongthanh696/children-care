package childrencare.app.repository;

import childrencare.app.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<PostModel,Integer> {
}
