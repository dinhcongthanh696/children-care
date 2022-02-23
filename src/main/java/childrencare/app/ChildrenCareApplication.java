package childrencare.app;




import javax.transaction.Transactional;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;




@SpringBootApplication
@Transactional
public class ChildrenCareApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);
		/*ReservationRepository reservationRepository = context.getBean(ReservationRepository.class);

		ReservationModel test = reservationRepository.getReservationModelByReservationId(1);
		System.out.println("12345678987665432");
		System.out.println(test.getFullname());*/
		
		
	}

}
