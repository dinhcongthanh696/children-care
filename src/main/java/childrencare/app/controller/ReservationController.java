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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

	// Thanh Code
	private final int DAYINSECONDS = 3600 * 24;

	//Nghia's code
	@Autowired
	private ReservationService reservationService;

	//Nghia's code
	@Autowired
	private Service_service service;


	// Thanh's code
	@Autowired
	private ServiceModelService serviceModelService;


	@GetMapping
	public String getServiceCarts(Model model, HttpSession session) {
		List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
		if (itemList != null) {
			double toalReservationPrice = 0;
			for (ServiceModel sm : itemList) {
				toalReservationPrice += sm.getTotalCost();
			}
			session.setAttribute("total",toalReservationPrice);
			model.addAttribute("total", toalReservationPrice);
		}
		model.addAttribute("list", itemList);
		return "reservationDetails";
	}

	@GetMapping("/add/{sid}")
	public String addToCart(@PathVariable(value = "sid") int id, Model model, HttpSession session,
							HttpServletResponse response, HttpServletRequest request ,
							@RequestParam(name = "quantity" , defaultValue = "1") int quantity) throws IOException {
		ServiceModel serviceById = service.getServiceById(id);
		List<ServiceModel> listReservations = null;
		if (session.getAttribute("list") == null) {
			listReservations = new ArrayList<>();
		} else {
			listReservations = (List<ServiceModel>) session.getAttribute("list");
		}
		boolean check = false;
		for (ServiceModel svm : listReservations) {
			if (svm.getServiceId() == id) {
				serviceById = svm;
				serviceById.setQuantity(serviceById.getQuantity()+quantity);
				check = true;
			}
		}
		if (check == false) {
			serviceById.setQuantity(quantity);
			listReservations.add(serviceById);
		}

		// start thanh's code (add service cookie)
		Cookie cartsCookie = CookieHandler.getCookie("carts", request);

		if (cartsCookie != null) {
			CookieHandler.editCartsCookie(id, request, response, "/", 7 * DAYINSECONDS, serviceById.toCookieValue());
		} else {
			CookieHandler.createNewCookie("carts", request, response, "/", 7 * DAYINSECONDS, serviceById.toCookieValue());
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

	//nghia's code
	@GetMapping("/contact")
	public String getReservationContact(Model model,HttpSession session) {
		List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
		double total = (Double) session.getAttribute("total");
		model.addAttribute("orderList",itemList);
		model.addAttribute("totalCheckOut",total);
		ReservationModel reservationModel = new ReservationModel();
		model.addAttribute("reservation",reservationModel);
		return "reservationContact";
	}

	//nghia's code
	//done save data to table reservation
	//save data to reservation_service - not done
	@PostMapping("/saveData")
	public String saveReservation(@Validated ReservationModel reservationModel, BindingResult result,
								  RedirectAttributes redirect, HttpSession session){
		if (result.hasErrors()) {
			return "redirect:/reservation/contact";
		}
		reservationModel.setTotalReservationPrice((Double) session.getAttribute("total"));
		reservationModel.setStatus(false);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		reservationModel.setDate(dtf.format(now));
		reservationService.save(reservationModel);
		return "";

	}
}
