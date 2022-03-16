package childrencare.app.repository;

import java.util.Date;
import java.util.List;

import childrencare.app.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import childrencare.app.model.CustomerModel;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer>{
	
	@Query(value = "SELECT COUNT(*) FROM customer inner join "
			+ "user_model on customer.customer_email = user_model.email WHERE DATEDIFF(day , register_date , GETDATE()) <= ?1" ,
			nativeQuery = true)
	public int countNewCustomerRegisterByLastDays(int days);
	
	@Query(value = "select COUNT(*) FROM\r\n"
			+ "(select customer.customer_id,customer.customer_email from customer \r\n"
			+ "inner join reservation on customer.customer_id = reservation.customer_id\r\n"
			+ "WHERE DATEDIFF(day , reservation.date , GETDATE()) <= ?1\r\n"
			+ "AND customer.customer_email NOT IN (SELECT DISTINCT c.customer_email FROM customer as c "
			+ "inner join reservation as r on c.customer_id = r.customer_id \r\n"
			+ "WHERE DATEDIFF(day, r.date , GETDATE()) > ?1 ) "
			+ "GROUP BY customer.customer_id,customer.customer_email) as reservedCustomers" , nativeQuery = true)
	public int countNewCustomerReservedByLastDays(int days);


	@Query(value = "Select * from customer where customer_email = ?1", nativeQuery = true)
	CustomerModel findByEmail(String email);
	
	@Query(value = "SELECT * FROM customer as c INNER JOIN user_model as u on c.customer_email = u.email "
			+ "WHERE (u.status > ?2 AND u.status < ?3) AND "
			+ "(u.phone LIKE ?1 OR u.fullname LIKE ?1 OR u.email LIKE ?1) ",
			countQuery = " SELECT COUNT(*) FROM customer as c inner join user_model as u on c.customer_email = u.email "
					+ "WHERE (u.status > ?2 AND u.status < ?3) AND "
					+ "(u.phone LIKE ?1 OR u.fullname LIKE ?1 OR u.email LIKE ?1)",
			nativeQuery = true)
	public Page<CustomerModel> findCustomerByStatusAndSearchQuery(String search,int startBitRange,int endBitRange,PageRequest pageRequest);

	@Modifying
	@Query(value = "Insert into customer values(1, ?1)", nativeQuery = true)
	void addNewCustomer(String email);

	@Modifying
	@Query(value = "INSERT INTO [customer]\n" +
			"           ([status]\n" +
			"           ,[customer_email]) VALUES\n" +
			"           (?1,?2)", nativeQuery = true)
	void insertToCus(int cStatus ,String email);

	@Modifying
	@Query(value = "INSERT INTO [customer_history]\n" +
			"           ([address]\n" +
			"           ,[customer_email]\n" +
			"           ,[fullname]\n" +
			"           ,[gender]\n" +
			"           ,[mobile]\n" +
			"           ,[updated_date]\n" +
			"           ,[customer_id]\n" +
			"           ,[email])\n" +
			"     VALUES\n" +
			"           (?1\n" +
			"           ,?2\n" +
			"           ,?3\n" +
			"           ,?4\n" +
			"           ,?5\n" +
			"           ,?6\n" +
			"           ,?7\n" +
			"           ,?8)", nativeQuery = true)
	void insertToCusHistory(String address,String customer_email,String fullname,boolean gender,String mobile,Date updated_date,int customer_id,String updated_by);

	@Query(value = "select MAX(customer_id) from customer",nativeQuery = true)
	int lastIDCus();

	@Query(value = "SELECT [customer_id]\n" +
			"  FROM [customer]\n" +
			"  where customer_email = ?1",nativeQuery = true)
	public int getCusIdByEmail(String email);



}
