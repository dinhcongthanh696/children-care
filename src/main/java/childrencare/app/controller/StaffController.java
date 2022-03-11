package childrencare.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import childrencare.app.model.ServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/staff")
public class StaffController {
	private final ServiceModelService service_service;
	
	private final ReservationService reservationService;
	
	@Autowired
	public StaffController(ServiceModelService service_service,ReservationService reservationService) {
		this.service_service = service_service;
		this.reservationService = reservationService;
	}
	
	@RequestMapping("/medical-examination")
	public String toMedicalExamination(Model model) {
		List<ServiceModel> services = service_service.getServices(); 
		
		
		model.addAttribute("services", services);
		return "staff-medical-examination";
	}
}
