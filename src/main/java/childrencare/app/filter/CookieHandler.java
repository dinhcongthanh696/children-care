package childrencare.app.filter;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
	public static final int COOKIEEXPRIEDTIME = 3600 * 24 * 7;

	public static Cookie getCookie(String name,HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static void editCartsCookie(int serviceId,HttpServletRequest request,HttpServletResponse response,
			String path,int expriedTime,String modifiedValue) {
		Cookie cartCookie = getCookie("carts", request);
		if(cartCookie != null) {
			String cartCookieValue = cartCookie.getValue();
			String[] serviceCarts = cartCookieValue.split("[-]+");
			String[] serviceCartAttributes;
			int serviceCookieId;
			String cartModifiedCookieValue = "";
			boolean isExsisting = false;
			for(String serviceCart : serviceCarts) {
				serviceCartAttributes = serviceCart.split("_");
				serviceCookieId = Integer.parseInt(serviceCartAttributes[0]);
				if(serviceCookieId == serviceId) {
					isExsisting = true;
					if(modifiedValue.isEmpty()) continue;
					cartModifiedCookieValue += modifiedValue + "-";
				}else {
					cartModifiedCookieValue += serviceCart + "-";
				}
			}
			if(!isExsisting) {
				cartModifiedCookieValue += modifiedValue;
			}
			cartCookie.setPath(path);
			cartCookie.setValue(cartModifiedCookieValue);
			cartCookie.setMaxAge(expriedTime);
			response.addCookie(cartCookie);
		}
	}

	public static void createNewCookie(String name , HttpServletRequest request,HttpServletResponse response,
			String path,int expriedTime,String value) {
		Cookie cookie = new Cookie(name,value);
		cookie.setPath(path);
		cookie.setMaxAge(expriedTime);
		response.addCookie(cookie);
	}
}
