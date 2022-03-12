package childrencare.app.controller;

import java.util.ArrayList;
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
import childrencare.app.model.ReservationServiceDrugModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.model.StaffModel;
import childrencare.app.model.UserModel;
import childrencare.app.service.DrugService;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationServiceDrugService;
import childrencare.app.service.ReservationService_Service;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/staff")
public class StaffController {
	private final ServiceModelService service_service;
	private final int RESERVATIONSERVICESIZE = 5;
	
	private final ReservationService_Service reservationService_Service;
	
	private final DrugService drugService;
	
	private final ReservationServiceDrugService rsdService;
	
	@Autowired
	public StaffController(ServiceModelService service_service,
			ReservationService_Service reservationService_Service,
			DrugService drugService,
			ReservationServiceDrugService rsdService) {
		this.service_service = service_service;
		this.reservationService_Service = reservationService_Service;
		this.drugService = drugService;
		this.rsdService = rsdService;
	}
	
	@GetMapping("/medical-examination")
	public String toMedicalExamination(Model model,
		@RequestParam(name = "service" , required = false , defaultValue = "-1") int serviceId,
		@RequestParam(name = "drugs" , required = false , defaultValue = "") String drugIdValues , 
		@RequestParam(name = "page" , required = false , defaultValue = "0") int page ,
		HttpSession session) {
		List<Integer> drugIds = new ArrayList<>();
		if(!drugIdValues.isEmpty()) {
			String[] drugIdValuesSplit = drugIdValues.split("[,]");
			for(int i = 0 ; i < drugIdValuesSplit.length ; i++) {
				drugIds.add(Integer.parseInt(drugIdValuesSplit[i]));
			}
		}
		
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
	
	@GetMapping("/prescription")
	public String toPrescription(Model model,
			@RequestParam(name = "rid") int rid,
			@RequestParam(name = "sid") int sid) {
		List<ReservationServiceDrugModel> prescription = rsdService.findByReservationAndService(rid, sid);
		List<DrugModel> drugs = drugService.findAllDrugs();
		int totalPrice = 0;
		for(ReservationServiceDrugModel rsd : prescription) {
			totalPrice += rsd.getQuantity() * rsd.getDrug().getPrice();
		}
		
		model.addAttribute("prescription", prescription);
		model.addAttribute("drugs", drugs);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("rid", rid);
		model.addAttribute("sid", sid);
		return "staff-medical-examination-prescription";
	}
}
