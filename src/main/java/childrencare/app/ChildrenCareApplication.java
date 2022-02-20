package childrencare.app;


import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import childrencare.app.model.ReservationModel;
import childrencare.app.repository.ReservationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.ServiceCategoryService;
import childrencare.app.service.ServiceModelService;



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
