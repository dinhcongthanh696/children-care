package childrencare.app.API;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import childrencare.app.model.Slot;
import childrencare.app.model.UserModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceModelService;

@RestController
@RequestMapping(path = "/api-reservation")
public class ReservationAPI {

	// Thanh's code
		@Autowired
		private ServiceModelService serviceModelService;

		@Autowired
		private ReservationService reservationService;


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
		List<ServiceModel> listReservations = (List<ServiceModel>) session.getAttribute("list");
		if (listReservations == null) {
			listReservations = new ArrayList<>();
		}
		
		boolean check = false;
		for (ServiceModel svm : listReservations) {
			if (svm.getServiceId() == id) {
				service = svm;
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
		session.setAttribute("list", listReservations);
		return listReservations;
	}



}