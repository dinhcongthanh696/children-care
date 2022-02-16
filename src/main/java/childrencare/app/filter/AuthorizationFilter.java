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
		String[] servletPathSplit = httpRequest.getServletPath().split("/");
		String[] screenUrlSplit;
		for(PermissionModel permission : user.getUserRole().getPermissions()) {
			screenUrlSplit = permission.getScreen().getUrl().split("/");
			int splitCount = 0;
			int splitHigherLength = ((servletPathSplit.length >= screenUrlSplit.length) ? servletPathSplit.length : screenUrlSplit.length);
			int splitLength = ((servletPathSplit.length <= screenUrlSplit.length) ? servletPathSplit.length : screenUrlSplit.length);
			for(int i = 0 ; i < splitLength ; i++ ) {
				if(screenUrlSplit[i].equals("*") || screenUrlSplit[i].equals(servletPathSplit[i])) {
					splitCount++;
				}else {
					break;
				}
			}
				if(splitCount == splitHigherLength && httpRequest.getMethod().equalsIgnoreCase(permission.getScreen().getMethod())) {
					chain.doFilter(httpRequest, httpResponse);
					return;
				}
		}
		httpResponse.sendError(403);
	}

}
