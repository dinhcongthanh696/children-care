package childrencare.app.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceModelService;

@RestController
@RequestMapping(path = "/api-reservation")
public class ReservationAPI {

	// Thanh's code
		@Autowired
		private ServiceModelService serviceModelService;

	@GetMapping("/add/{sid}")
	public List<ServiceModel> addToCart(@PathVariable(value = "sid") int id, HttpSession session,
			HttpServletResponse response, HttpServletRequest request , 
			@RequestParam(name = "quantity" , defaultValue = "1") int quantity) throws IOException {
		Optional<ServiceModel> optinal = serviceModelService.getServiceById(id);
		ServiceModel service = null;

		if (optinal.isPresent()) {
			service = optinal.get();
			service.setBase64ThumbnailEncode(service.getThumbnail());
		} else {
			throw new RuntimeException(" Service not found for id :: " + id);
		}
		List<ServiceModel> listReservations = null;
		if (session.getAttribute("list") == null) {
			listReservations = new ArrayList<>();
		} else {
			listReservations = (List<ServiceModel>) session.getAttribute("list");
		}
		boolean check = false;
		for (ServiceModel svm : listReservations) {
			if (svm.getServiceId() == id) {
				service.setQuantity(service.getQuantity() + quantity);
				check = true;
			}
		}
		if (check == false) {
			service.setQuantity(quantity);
			listReservations.add(service);
		}
		// start thanh's code (add service cookie)
		Cookie cartsCookie = CookieHandler.getCookie("carts", request);

		if (cartsCookie != null) {
			CookieHandler.editCartsCookie(id, request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, service.toCookieValue());
		} else {
			CookieHandler.createNewCookie("carts", request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, service.toCookieValue());
		}

		// end thanh's code (Add service cookie)

		return listReservations;
	}
}