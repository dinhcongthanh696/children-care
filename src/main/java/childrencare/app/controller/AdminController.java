package childrencare.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ServiceCategoryService;
import childrencare.app.service.ServiceModelService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final ServiceModelService serviceModelSerivce;
	private final int SERVICESIZE = 6;
	private final ServiceCategoryService serviceCategoryService;
	private final ReservationService reservationService;
	public AdminController(ServiceModelService serviceModelService,ServiceCategoryService serviceCategoryService,
			ReservationService reservationService) {
		this.serviceModelSerivce = serviceModelService;
		this.serviceCategoryService = serviceCategoryService;
		this.reservationService = reservationService;
	}
	
	
	@RequestMapping(value = "/services", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchServiceListByTitle(Model model,
			@RequestParam(name =  "page",required = false, defaultValue = "0") int page,
			@RequestParam(name =  "search",required = false, defaultValue = "") String search,
			@RequestParam(name = "status",required = false,defaultValue = "") String rawStatus,
			@RequestParam(name = "sort" , required = false , defaultValue = "title") String sort) {
		Page<ServiceModel> services;
		int startBitRangeValue = -1;
		int endBitRangeValue = 2;
		if(rawStatus.equals("true")) {
			startBitRangeValue = 0;
		}else if(rawStatus.equals("false")) {
			endBitRangeValue = 1;
		}
		services = serviceModelSerivce.getServicesPaginated(page, SERVICESIZE, startBitRangeValue, endBitRangeValue, search,sort);
		
		List<ServiceCategoryModel> categories = serviceCategoryService.findAll();
		
		model.addAttribute("services",services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("search", search);
		model.addAttribute("status", rawStatus);
		model.addAttribute("categories", categories);
		model.addAttribute("sort", sort);
		return "manager-service-list";
	}
	
	@GetMapping("/dashboard")
	public String toAdminDashBoard(Model model,
			@RequestParam(name = "revenueDate",required = false,defaultValue = "") String revenueDate,
			@RequestParam(name = "page",required = false,defaultValue = "0" ) Integer page) {
		int totalReservationSuccess = reservationService.countReservationByStatus(1);
		int totalReservationCanceled = reservationService.countReservationByStatus(0);
		
		Calendar calendar = Calendar.getInstance();
		String month = "";
		if(revenueDate.isEmpty()) {
			month = (calendar.get(Calendar.MONTH) + 1) < 10 ? ("0"+(calendar.get(Calendar.MONTH) + 1) ) : (calendar.get(Calendar.MONTH) + 1) + "";
			revenueDate =  calendar.get(Calendar.YEAR)+"-"+month;
		}else {
			String[] revenueSplit = revenueDate.split("[-]");
			calendar.set(Calendar.YEAR, Integer.parseInt(revenueSplit[0]));
			calendar.set(Calendar.MONTH, Integer.parseInt(revenueSplit[1]) - 1);
		}
		List<ServiceCategoryModel> categories = serviceCategoryService.findByDate(calendar);
		
		Page<ServiceModel> services = serviceModelSerivce.getServicesPaginated(page, SERVICESIZE, "", "title");
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DAY_OF_YEAR, -6);
		List<String> sevenLastDays = new ArrayList<>();
		
		List<Integer> reservationSuccessNumbers = new ArrayList<>();
		List<Integer> reservationCanceledNumbers = new ArrayList<>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		for(int i = 0 ; i < 7 ; i++) {
			sevenLastDays.add(dateFormatter.format(currentDate.getTime()));
			reservationSuccessNumbers.add(reservationService.countReservationByStatusAndDate(1, currentDate.getTime()));
			reservationCanceledNumbers.add(reservationService.countReservationByStatusAndDate(0, currentDate.getTime()));
			currentDate.add(Calendar.DAY_OF_YEAR, 1);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		
		model.addAttribute("reservationsSuccess",totalReservationSuccess);
		model.addAttribute("reservationsCanceled", totalReservationCanceled);
		model.addAttribute("categories", categories);
		model.addAttribute("revenueDate", revenueDate);
		model.addAttribute("services", services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("reservationSuccessNumbers", reservationSuccessNumbers);
		model.addAttribute("reservationCanceledNumbers", reservationCanceledNumbers);
		try {
			model.addAttribute("sevenLastDays", objectMapper.writeValueAsString(sevenLastDays));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "admin-dashboard";
	}
	
}
