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
import javax.transaction.Transactional;

import childrencare.app.model.ReservationModel;
import childrencare.app.service.ReservationService;
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
	public int addToCart(@PathVariable(value = "sid") int id, HttpSession session,
						 HttpServletResponse response, HttpServletRequest request,
						 @RequestParam(name = "quantity", defaultValue = "1") int quantity) throws IOException {
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
		return listReservations.size();
	}


	@Transactional
	@PutMapping("/schedule")
	public void addSchedule(HttpSession session,
							@RequestParam(name = "reservation_id") Integer reservationId,
							@RequestParam(name = "service_id") Integer serviceId,
							@RequestParam(name = "slot_id") Integer slotId,
							@RequestParam(name = "staff_id") Integer staffId,
							@RequestParam(name = "booked_date") Date date
	) {
		double price = serviceModelService.getServiceById(serviceId).get().getOriginalPrice();
		List<ServiceModel> serviceModels = (List<ServiceModel>) session.getAttribute("list");
		for (int i = 0; i < serviceModels.size(); i++) {
			if (serviceModels.get(i).getServiceId() == serviceId) {
				int quantity = serviceModels.get(i).getQuantity();
				if (quantity == 1) {
					serviceModels.remove(serviceModels.get(i));
				} else {
					serviceModels.get(i).setQuantity(quantity - 1);
				}
			}
		}
		reservationService.createSchedule(reservationId, serviceId, slotId, staffId, date, price);
	}


	//Delete reservation service
	@Transactional
	@DeleteMapping("/schedule")
	public void deleteSchedule(HttpSession session,
							   @RequestParam(name = "slot_id") Integer slot_id,
							   @RequestParam(name = "staff_id") Integer staff_id,
							   @RequestParam(name = "booked_date") Date booked_date,
							   @RequestParam(name = "service_id") Integer serviceId
	) {
		List<ServiceModel> serviceModels = (List<ServiceModel>) session.getAttribute("list");
		boolean isExist = false;
			for (int i = 0; i < serviceModels.size(); i++) {
				ServiceModel s = serviceModels.get(i);
				if (s.getServiceId() == serviceId) {
					s.setQuantity(s.getQuantity() + 1);
					isExist = true;
					break;
				}
			}
			if (isExist == false) {
				ServiceModel serviceModel = serviceModelService.findServiceById(serviceId);
				serviceModel.setQuantity(1);
				serviceModels.add(serviceModel);
			}
			reservationService.deleteSchedule(slot_id, staff_id, booked_date);
		}
	}