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

	//Nghia's code
	@GetMapping("/cartDetails")
	public String getReservation(Model model, HttpSession session) {
		List<ReservationServiceModel> itemList = (List<ReservationServiceModel>) session.getAttribute("list");

		if (itemList != null) {
			double toalReservationPrice = 0;
			for (ReservationServiceModel rsm : itemList) {
				double total = rsm.getReservation().getTotalReservationPrice();
				total += rsm.totalCost();
				toalReservationPrice += total;
			}
			session.setAttribute("total",toalReservationPrice);
			model.addAttribute("total", toalReservationPrice);
		}
		model.addAttribute("Order", itemList);
		return "reservationDetails";
	}


	//Nghia's code - edit add service to reservation
	@GetMapping("/add/{sid}")
	public String addToCart(@PathVariable(value = "sid") int id, Model model, HttpSession session) {

		//get service from service list
		ServiceModel serviceModel = service.getServiceById(id);
		List<ReservationServiceModel> listReservations = null;
		if (session.getAttribute("list") == null) {
			listReservations = new ArrayList<>();
		} else {
			listReservations = (List<ReservationServiceModel>) session.getAttribute("list");
		}
		boolean check = false;
		for (ReservationServiceModel svm : listReservations) {
			if (svm.getService().getServiceId() == id) {
				svm.setTotalPerson(svm.getTotalPerson() + 1);
				check = true;
			}
		}
		if (check == false) {
			ReservationModel reservation = new ReservationModel();
			reservation.setReservationId(reservationService.idIdentity()+1);
			ServiceModel serviceModel1 = serviceModel;
			ReservationServiceModel reservationModel = new ReservationServiceModel();
			reservationModel.setTotalPerson(1);
			reservationModel.setReservation(reservation);
			reservationModel.setService(serviceModel1);
			listReservations.add(reservationModel);
		}
		session.setAttribute("list", listReservations);
		return "redirect:/reservation/cartDetails";
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

	//nghia's code
	@GetMapping("/delete/{id}")
	public String deleteFromCart(@PathVariable(value = "id") int id, Model model, HttpSession session) {
		//get service from service list
		List<ReservationServiceModel> listReservation =
				(List<ReservationServiceModel>) session.getAttribute("list");
		if (listReservation != null) {
			for (ReservationServiceModel serviceModel : listReservation) {
				if (serviceModel.getService().getServiceId() == id) {
					listReservation.remove(serviceModel);
					session.setAttribute("list", listReservation);
					return "redirect:/reservation/cartDetails";
				}
			}
		}
		return "";
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
		List<ReservationServiceModel> itemList = (List<ReservationServiceModel>) session.getAttribute("list");
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
		reservationService.save(reservationModel);
		//save data to reservation_service - error(unsaved to reservation_service)
		List<ReservationServiceModel> listReservation = (List<ReservationServiceModel>) session.getAttribute("list");
		for (ReservationServiceModel item: listReservation){
			reservationService.insertReservation_Service(reservationService.idIdentity(),
					item.getService().getServiceId(),item.getTotalPerson());
		}
		//save data to reservation_service - error
		return "redirect:/reservation/list";

	}
}
