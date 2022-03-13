package childrencare.app.repository;

import childrencare.app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Payment, Integer> {

    @Modifying
    @Query(value = "UPDATE reservation set payment_id = ?1 where reservation_id = ?2", nativeQuery = true)
    void setPaymentMethod(int pid, int rid);
}
