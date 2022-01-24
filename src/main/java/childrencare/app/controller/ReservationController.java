package childrencare.app.controller;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ServiceModelService;
import childrencare.app.service.Service_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	// Thanh Code
	private final int DAYINSECONDS = 3600 * 24;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private Service_service service;

	// Thanh's code
	@Autowired
	private ServiceModelService serviceModelService;

	@GetMapping("/details")
	public String getHome(Model model) {
		model.addAttribute("reservations", reservationService.findAll());
		return "reservationDetails";
	}

	@GetMapping
	public String getServiceCarts(Model model, HttpSession session) {
		List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
		model.addAttribute("list", services);
		return "reservationDetails";
	}

	@GetMapping("/add/{sid}")
	public String addToCart(@PathVariable(value = "sid") int id, Model model, HttpSession session,
			HttpServletResponse response, HttpServletRequest request , 
			@RequestParam(name = "quantity" , defaultValue = "1") int quantity) throws IOException {
		Optional<ServiceModel> optinal = service.findById(id);
		ServiceModel service = null;
		if (optinal.isPresent()) {
			service = optinal.get();
			service.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(service.getThumbnail()));
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
				service = svm;
				service.setQuantity(quantity);
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
			CookieHandler.editCartsCookie(id, request, response, "/", 7 * DAYINSECONDS, service.toCookieValue());
		} else {
			CookieHandler.createNewCookie("carts", request, response, "/", 7 * DAYINSECONDS, service.toCookieValue());
		}
		// end thanh's code (Add service cookie)
		session.setAttribute("list", listReservations);
		model.addAttribute("list", listReservations);
		return "reservationDetails";
	}
	
	@DeleteMapping("/delete/{sid}")
	public void deleteFromCart(@PathVariable(value = "sid") int id,HttpSession session,
			HttpServletResponse response, HttpServletRequest request ) {
		List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
		for(ServiceModel service : services) {
			if(service.getServiceId() == id) {
				services.remove(service);
				break;
			}
		}
		CookieHandler.editCartsCookie(id, request, response, "/", 7 * DAYINSECONDS, "");
		
	}
}
