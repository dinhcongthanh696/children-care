package childrencare.app;




import javax.transaction.Transactional;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import childrencare.app.model.CustomerModel;
import childrencare.app.repository.CustomerRepository;




@SpringBootApplication
@Transactional
public class ChildrenCareApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);
		/*ReservationRepository reservationRepository = context.getBean(ReservationRepository.class);

		ReservationModel test = reservationRepository.getReservationModelByReservationId(1);
		System.out.println("12345678987665432");
		System.out.println(test.getFullname());*/
		
		CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
		Page<CustomerModel> customersPageable = customerRepository.findCustomerByStatusAndSearchQuery("%%",
				-1, 2, PageRequest.of(0, 2, Sort.by(Direction.ASC,"u.fullname","u.email","u.status","u.phone") ) );
		customersPageable.toList().stream().forEach(customer -> {
			System.out.println("Customer : "+customer.getCustomer_user().getFullname());
			
		});
		
	}

}
