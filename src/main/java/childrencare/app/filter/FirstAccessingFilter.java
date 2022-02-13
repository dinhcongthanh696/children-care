package childrencare.app.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceModelService;

@Component
public class FirstAccessingFilter implements Filter {
	@Autowired                              
	private ServiceModelService serviceModelService;

	public FirstAccessingFilter() {
		super();
	}



	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		Cookie cookieCarts = CookieHandler.getCookie("carts", servletRequest);

		HttpSession session = servletRequest.getSession();
		List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
		if (services == null && cookieCarts != null) {
			String cartsValue = cookieCarts.getValue();
			String[] serviceCarts_split = cartsValue.split("[-]");
			String[] serviceCartAttributes;
			ServiceModel service;
			List<ServiceModel> serviceCarts = new ArrayList<>();
			for (String serviceCart : serviceCarts_split) {
				serviceCartAttributes = serviceCart.split("[_]");
				service = serviceModelService.getServicesById(Integer.parseInt(serviceCartAttributes[0]));
				service.setBase64ThumbnailEncode(service.getThumbnail());
				service.setQuantity(Integer.parseInt(serviceCartAttributes[1]));
				serviceCarts.add(service);
			}
			session.setAttribute("list", serviceCarts);
		}
		chain.doFilter(servletRequest, response);
	}
}
