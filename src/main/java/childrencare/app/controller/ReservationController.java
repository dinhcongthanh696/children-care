package childrencare.app.controller;

import childrencare.app.filter.CookieHandler;
import childrencare.app.model.ReservationModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.LoginService;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ServiceModelService;
import childrencare.app.service.Service_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	// Nghia's code

	// Thanh Code
	private final int DAYINSECONDS = 3600 * 24;

	//Nghia's code
	@Autowired
	private ReservationService reservationService;


	// Thanh's code
	@Autowired
	private ServiceModelService serviceModelService;
	
	@Autowired
	private Service_service service;

/*	@GetMapping
	public String getServiceCarts(Model model, HttpSession session) {
		List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
		model.addAttribute("list", services);
======= */
	@Autowired
	private LoginService loginService;


	@GetMapping("/reser")
	public String getServiceCarts(Model model, HttpSession session) {
		List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
		if (itemList != null) {
			double toalReservationPrice = 0;
			for (ServiceModel sm : itemList) {
				toalReservationPrice += sm.getTotalCost();
			}
			session.setAttribute("total",toalReservationPrice);
			model.addAttribute("size",itemList.size());
		}
		model.addAttribute("list", itemList);
		return "reservationDetails";
	}

	@GetMapping("/add/{sid}")
	public String addToCart(@PathVariable(value = "sid") int id, Model model, HttpSession session,
/*<<<<<<< HEAD
			HttpServletResponse response, HttpServletRequest request ,
			@RequestParam(name = "quantity", defaultValue = "1") int quantity) {
		Optional<ServiceModel> optinal = serviceModelService.getServiceById(id);
		ServiceModel service = null;
		if (optinal.isPresent()) {
			service = optinal.get();
			service.setBase64ThumbnailEncode(service.getThumbnail());
		} else {
			throw new RuntimeException(" Service not found for id :: " + id);
		}
======= */
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
				serviceById.setQuantity(serviceById.getQuantity() + quantity);
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
			CookieHandler.editCartsCookie(id, request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, serviceById.toCookieValue());
		}else {
			CookieHandler.createNewCookie("carts", request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, serviceById.toCookieValue());
		}
		// end thanh's code (Add service cookie)
		session.setAttribute("list", listReservations);  
		model.addAttribute("list", listReservations);
		return "reservationDetails";
	}

/*<<<<<<< HEAD
// thanh's code
	@DeleteMapping("/delete/{sid}")
	public void deleteFromCart(@PathVariable(value = "sid") int id, HttpSession session, HttpServletResponse response,
			HttpServletRequest request) { */
	@DeleteMapping("/delete/{sid}")
	public void deleteFromCart(@PathVariable(value = "sid") int id,HttpSession session,
							   HttpServletResponse response, HttpServletRequest request ) {
		List<ServiceModel> services = (List<ServiceModel>) session.getAttribute("list");
		for (ServiceModel service : services) {
			if (service.getServiceId() == id) {
				services.remove(service);
				break;
			}
		}
		CookieHandler.editCartsCookie(id, request, response, "/", CookieHandler.COOKIEEXPRIEDTIME, "");
	}



	//nghia's code
	@GetMapping("/contact")
	public String getReservationContact(Model model,HttpSession session) {
		List<ServiceModel> itemList = (List<ServiceModel>) session.getAttribute("list");
		UserModel userModel = (UserModel) session.getAttribute("user");
		if(itemList!= null){
			double total = (Double) session.getAttribute("total");
			model.addAttribute("orderList",itemList);
			model.addAttribute("total",total);
			ReservationModel reservationModel = new ReservationModel();
			model.addAttribute("reservation",reservationModel);
			model.addAttribute("user",userModel);
			return "reservationContact";
		}else{
			return "redirect:/reservation/reser";
		}


	}

	//nghia's code
	@PostMapping("/saveData")
	@Transactional
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
		// thanh's code
		int rid = reservationService.saveReservation(reservationModel);
		List<ServiceModel> serviceCarts = (List<ServiceModel>) session.getAttribute("list");
		for(ServiceModel serviceLoop : serviceCarts) {
			reservationService.insertReservation_Service(rid, serviceLoop.getServiceId(),1);
		}
		
		return "redirect:/reservation";
		// end thanh's code
	}




}
