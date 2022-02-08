package childrencare.app.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import childrencare.app.filter.LoggingFilter;

@Configuration
public class MyConfiguration {
	private LoggingFilter loggingFilter;
	
	@Autowired
	public MyConfiguration(LoggingFilter loggingFilter) {
		this.loggingFilter = loggingFilter;
	}
	@Bean
	public FilterRegistrationBean<LoggingFilter> filterRegister(){
		FilterRegistrationBean<LoggingFilter> register = new FilterRegistrationBean<LoggingFilter>();
		register.setFilter(loggingFilter);
		register.setOrder(1);
		register.addUrlPatterns("/*");
		return register;
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("thanhdche150982@fpt.edu.vn");
	    mailSender.setPassword("dinhcongthanh");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
		return mailSender;
	}

}
