package childrencare.app;

import java.awt.print.Pageable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


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
	}

}
