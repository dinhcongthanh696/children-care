package childrencare.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import childrencare.app.model.DrugModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.StaffModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.DrugService;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationService_Service;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/staff")
public class StaffController {
	private final ServiceModelService service_service;
	private final int RESERVATIONSERVICESIZE = 5;
	
	private final ReservationService_Service reservationService_Service;
	
	private final DrugService drugService;
	
	@Autowired
	public StaffController(ServiceModelService service_service,
			ReservationService_Service reservationService_Service,
			DrugService drugService) {
		this.service_service = service_service;
		this.reservationService_Service = reservationService_Service;
		this.drugService = drugService;
	}
	
	@GetMapping("/medical-examination")
	public String toMedicalExamination(Model model,
		@RequestParam(name = "service" , required = false , defaultValue = "-1") int serviceId,
		@RequestParam(name = "drugs" , required = false) List<Integer> drugIds , 
		@RequestParam(name = "page" , required = false , defaultValue = "0") int page ,
		HttpSession session) {
		List<ServiceModel> services = service_service.getServices();
		UserModel staff = (UserModel) session.getAttribute("user"); 
		Page<ReservationServiceModel> reservationServicesPageable = 
				reservationService_Service.findReservationServiceByStaffAndServiceAndDrugs(page, RESERVATIONSERVICESIZE, staff.getStaff().getStaff_id(), serviceId, drugIds);
		for(ReservationServiceModel reservationService : reservationServicesPageable.toList()) {
			int rid = reservationService.getReservation().getReservationId();
			int sid = reservationService.getService().getServiceId();
			reservationService.setDrugs(drugService.findByReservationAndService(rid,sid));
		} 
		
		List<DrugModel> drugs = drugService.findAllDrugs();
		model.addAttribute("drugs", drugs);
		model.addAttribute("totalPages", reservationServicesPageable.getTotalPages());
		model.addAttribute("currentPage", reservationServicesPageable.getNumber());
		model.addAttribute("reservationServices", reservationServicesPageable.toList());
		model.addAttribute("services", services);
		model.addAttribute("currentServiceId", serviceId);
		model.addAttribute("currentDrugIdS", drugIds);
		return "staff-medical-examination";
	}
}
