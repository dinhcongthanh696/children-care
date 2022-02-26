package childrencare.app.repository;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends JpaRepository<UserModel,String> {

    @Query(value = "select * from user_model where\n" +
            "(username = ?1 or email = ?1)\n" +
            "and password = ?2",nativeQuery = true)
    UserModel checkUserExist(String input,String password);

    @Query(value = "select * from user_model where\n" +
            "email = ?1 or username = ?2",nativeQuery = true)
    UserModel checkUserRegister(String email,String username);

    @Query(value = "select * from user_model where username = ?1",nativeQuery = true)
    UserModel getInfo(String username);

    @Modifying
    @Query(value = "update user_model set password = ?1 where username = ?2",nativeQuery = true)
    void changePass(String pass,String username);





}
