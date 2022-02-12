package childrencare.app;


import javax.transaction.Transactional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;



@SpringBootApplication
@Transactional
public class ChildrenCareApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChildrenCareApplication.class, args);

	}

}
