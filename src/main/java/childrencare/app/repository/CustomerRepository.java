package childrencare.app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer>{
	
	@Query(value = "SELECT COUNT(*) FROM user_model WHERE DATEDIFF(day , register_date , GETDATE()) <= ?1" ,
			nativeQuery = true)
	public int countNewCustomerRegisterByLastDays(int days);
	
	@Query(value = "select COUNT(*) FROM\r\n"
			+ "(select customer.customer_id,customer.customer_email from customer \r\n"
			+ "inner join reservation on customer.customer_id = reservation.customer_id\r\n"
			+ "WHERE DATEDIFF(day , reservation.date , GETDATE()) <= ?1\r\n"
			+ "GROUP BY customer.customer_id,customer.customer_email) as reservedCustomers" , nativeQuery = true)
	public int countNewCustomerReservedByLastDays(int days);


}
