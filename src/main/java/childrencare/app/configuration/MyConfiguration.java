package childrencare.app.configuration;

import java.util.Locale;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import childrencare.app.filter.AuthorizationFilter;
import childrencare.app.filter.FirstAccessingFilter;

@Configuration
public class MyConfiguration implements WebMvcConfigurer{
	private FirstAccessingFilter firstAccessingFilter;
	private AuthorizationFilter authorizarionFilter;
	@Autowired
	public MyConfiguration(FirstAccessingFilter firstAccessingFilter,AuthorizationFilter authorizationFilter) {
		this.firstAccessingFilter = firstAccessingFilter;
		this.authorizarionFilter = authorizationFilter;
	}
	@Bean
	public FilterRegistrationBean<FirstAccessingFilter> firstAccessingfilterRegister(){
		FilterRegistrationBean<FirstAccessingFilter> register = new FilterRegistrationBean<FirstAccessingFilter>();
		register.setFilter(firstAccessingFilter);
		register.setOrder(1);
		register.addUrlPatterns("/*");
		return register;
	}
	
	@Bean
	public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegister(){
		FilterRegistrationBean<AuthorizationFilter> register = new FilterRegistrationBean<AuthorizationFilter>();
		register.setFilter(authorizarionFilter);
		register.setOrder(2);
		register.addUrlPatterns("/admin/*");
		return register;
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("ankvhe150757@fpt.edu.vn");
	    mailSender.setPassword("an17122001");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
		return mailSender;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.forLanguageTag("vi"));
	    return slr;
	}
	
	@Bean 
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	

}
