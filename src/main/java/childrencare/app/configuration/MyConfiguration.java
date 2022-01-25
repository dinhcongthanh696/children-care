package childrencare.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import childrencare.app.filter.LoggingFilter;

@Configuration
public class MyConfiguration {
	@Autowired
	private LoggingFilter loggingFilter;

	@Bean
	public FilterRegistrationBean filterRegister(){
		FilterRegistrationBean register = new FilterRegistrationBean();
		register.setFilter(loggingFilter);
		register.setOrder(1);
		register.addUrlPatterns("/*");
		return register;
	}

}
