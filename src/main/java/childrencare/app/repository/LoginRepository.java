package childrencare.app.repository;


import childrencare.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<UserModel,String> {

    @Query(value = "select * from user_model where\n" +
            "username = ?1 and email = ?2\n" +
            "and password = ?3",nativeQuery = true)
    UserModel checkUserExist(String username,String email, String password);

    @Query(value = "select * from user_model where username = ?1",nativeQuery = true)
    UserModel getInfo(String username);



}