package childrencare.app.filter;

import java.io.IOException;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import childrencare.app.model.PermissionModel;
import childrencare.app.model.UserModel;

@Component
public class AuthorizationFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		UserModel user = (UserModel) session.getAttribute("user");
		if(user == null) {
			httpResponse.sendError(401);
			return;
		}
		String to = httpRequest.getServletPath();
		for(PermissionModel permission : user.getUserRole().getPermissions()) {
			if(permission.getScreen().getUrl().equals(to)) {
				chain.doFilter(httpRequest, httpResponse);
				return;
			}
		}
		httpResponse.sendError(403);
	}

}
