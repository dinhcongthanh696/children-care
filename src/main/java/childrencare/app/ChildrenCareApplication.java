package childrencare.app;





import java.util.List;

import javax.transaction.Transactional;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;

import childrencare.app.model.DrugModel;
import childrencare.app.model.ReservationServiceDrugModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.repository.ReservationServiceRepository;




@SpringBootApplication
@Transactional
public class ChildrenCareApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);
		/*ReservationRepository reservationRepository = context.getBean(ReservationRepository.class);

		ReservationModel test = reservationRepository.getReservationModelByReservationId(1);
		System.out.println("12345678987665432");
		System.out.println(test.getFullname());*/
		
	/*	ReservationServiceRepository repository = context.getBean(ReservationServiceRepository.class);
		
		
		List<ReservationServiceModel> services = 
		repository.listReservationByStaffAndServiceAndDrugs(1, -1, null, PageRequest.of(0, 3)).toList();
		
		for(ReservationServiceModel service : services) {
			System.out.println("Service Id : "+service.getService().getServiceId() 
					+ " Reservation Id : "+service.getReservation().getReservationId() + "   Drugs : ");
			for(ReservationServiceDrugModel drug : service.getReservation().getReservationServiceDrugs()) {
				System.out.println("-  "+drug.getDrug().getDrugName());
			}
		} */
	}

}
