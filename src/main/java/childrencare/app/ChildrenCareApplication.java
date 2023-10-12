package childrencare.app;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;

import childrencare.app.model.DrugModel;
import childrencare.app.model.ReservationServiceDrugModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.repository.ReservationServiceDrugRepository;
import childrencare.app.repository.ReservationServiceRepository;
import childrencare.app.service.DrugService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

@SpringBootApplication
public class ChildrenCareApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);
		/*
		 * ReservationRepository reservationRepository =
		 * context.getBean(ReservationRepository.class);
		 * 
		 * ReservationModel test =
		 * reservationRepository.getReservationModelByReservationId(1);
		 * System.out.println("12345678987665432");
		 * System.out.println(test.getFullname());
		 */

		//tam biet ae nhe
	}

}
