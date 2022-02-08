package childrencare.app;


import javax.transaction.Transactional;

import childrencare.app.model.FeedbackModel;
import childrencare.app.repository.FeedbackRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;

import java.util.List;


@SpringBootApplication
@Transactional
public class ChildrenCareApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);
		// test commit
		// hehehehehehehehehe
		// hello duc anh
		// hello duc 2
		// duc anh 16/1
		ServiceModel serviceModel = new ServiceModel();
		FeedbackRepository feedbackRepository = context.getBean(FeedbackRepository.class);
		serviceModel.setServiceId(1);
		List<FeedbackModel> feedbackModels = feedbackRepository.findByService(serviceModel);
		for (FeedbackModel feedbackModel:feedbackModels
		) {
			System.out.print(feedbackModel.getComment());
		}
	}

}
