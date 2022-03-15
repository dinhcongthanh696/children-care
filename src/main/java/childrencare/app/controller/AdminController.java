package childrencare.app.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import childrencare.app.model.*;
import childrencare.app.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final ServiceModelService serviceModelSerivce;
	private final int SERVICESIZE = 6;
	private final int USERSIZE = 3;
	private final ServiceCategoryService serviceCategoryService;
	private final ReservationService reservationService;
	private final CustomerService customerService;
	private final UserService userService;
	public AdminController(ServiceModelService serviceModelService,
						   ServiceCategoryService serviceCategoryService,
						   ReservationService reservationService,
						   CustomerService customerService, UserService userService) {
		this.serviceModelSerivce = serviceModelService;
		this.serviceCategoryService = serviceCategoryService;
		this.reservationService = reservationService;
		this.customerService = customerService;
		this.userService = userService;
	}
	
	
	@GetMapping("/dashboard")
	public String toAdminDashBoard(Model model,
			@RequestParam(name = "revenueDate",required = false,defaultValue = "") String revenueDate,
			@RequestParam(name = "page",required = false,defaultValue = "0" ) Integer page) {
		int totalCustomerNewlyReserved = customerService.getNewCustomerReservedByLastDays(7);
		int totalCustomerNewlyRegistered = customerService.getNewCustomerRegisterByLastDays(7);
		
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
		
		Page<ServiceModel> services = serviceModelSerivce.getServicesPaginatedAndFeedbacksByLastDays(page, SERVICESIZE, 7, "title");
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DAY_OF_YEAR, -6);
		List<String> sevenLastDays = new ArrayList<>();
		
		List<Integer> reservationSuccessNumbers = new ArrayList<>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		List<Integer> reservationTotalNumbers = new ArrayList<>();
		int totalReservationCanceledLast7days = 0;
		int totalReservationSubmittedLast7days = 0;
		int totalReservationSuccessLast7days = 0;
		int totalReservationCanceledLast14days = 0;
		int totalReservationSubmittedLast14days = 0;
		int totalReservationSuccessLast14days = 0;
		
		Calendar tempCalendar = calendar.getInstance();
		for(int i = 0 ; i < 7 ; i++) {
			sevenLastDays.add(dateFormatter.format(currentDate.getTime()));
			tempCalendar.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 7);
			int reservationSuccess = reservationService.countReservationByStatusAndDate("success", currentDate.getTime());
			int reservationCanceled = reservationService.countReservationByStatusAndDate("cancled", currentDate.getTime());
			int reservationSubmitted = reservationService.countReservationByStatusAndDate("submitted", currentDate.getTime());
			totalReservationCanceledLast14days += reservationService.countReservationByStatusAndDate("cancled",tempCalendar.getTime());
			totalReservationSuccessLast14days += reservationService.countReservationByStatusAndDate("success", tempCalendar.getTime());
			totalReservationSubmittedLast14days += reservationService.countReservationByStatusAndDate("submitter", tempCalendar.getTime());
			
			totalReservationSubmittedLast7days += reservationSubmitted;
			totalReservationCanceledLast7days += reservationCanceled;
			totalReservationSuccessLast7days += reservationSuccess;
			reservationSuccessNumbers.add(reservationSuccess);
			reservationTotalNumbers.add(reservationSuccess+reservationCanceled);
			currentDate.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		model.addAttribute("totalCustomerNewlyRegistered", totalCustomerNewlyRegistered);
		model.addAttribute("totalCustomerNewlyReserved", totalCustomerNewlyReserved);
		model.addAttribute("reservationsSuccess",totalReservationSuccessLast7days);
		model.addAttribute("reservationsCanceled", totalReservationCanceledLast7days);
		model.addAttribute("reservationsSubmitted", totalReservationSubmittedLast7days);
		model.addAttribute("totalReservationCanceledLast14days", totalReservationCanceledLast14days);
		model.addAttribute("totalReservationSuccessLast14days", totalReservationSuccessLast14days);
		model.addAttribute("totalReservationSubmittedLast14days", totalReservationSubmittedLast14days);
		model.addAttribute("categories", categories);
		model.addAttribute("revenueDate", revenueDate);
		model.addAttribute("services", services.toList());
		model.addAttribute("totalPages",services.getTotalPages());
		model.addAttribute("currentPage",services.getNumber());
		model.addAttribute("reservationSuccessNumbers", reservationSuccessNumbers);
		model.addAttribute("reservationTotalNumbers", reservationTotalNumbers);
		try {
			model.addAttribute("sevenLastDays", objectMapper.writeValueAsString(sevenLastDays));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index_admin";
	}

	@RequestMapping(value = "/feedback", method = { RequestMethod.GET, RequestMethod.POST })
	public String show() {
		return "manager-feedback-list";
	}

	@GetMapping("/users")
	@Transactional
	public String toUsersList(@RequestParam(name = "search", required = false, defaultValue = "") String search,
								  @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
								  @RequestParam(name = "directions", required = false, defaultValue = "ascending,ascending,ascending,ascending") String directionsParam,
								  @RequestParam(name = "sortProperty", required = false, defaultValue = "email") String sortProperty,
								  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
								  Model model) {
		int startBitRange = -1;
		int endBitRange = 2;
		switch (status) {
			case 0:
				endBitRange--;
				break;
			case 1:
				startBitRange++;
				break;
		}

		String[] directionsValue = directionsParam.split("[,]");
		Sort.Direction[] directions = new Sort.Direction[directionsValue.length];
		for (int i = 0; i < directionsValue.length; i++) {
			directions[i] = (directionsValue[i].equals("ascending")) ? Sort.Direction.ASC : Sort.Direction.DESC;
		}
		LinkedList<String> sortProperties = new LinkedList<String>(Arrays.asList("email", "fullname", "phone", "role_id"));
		//Collections.swap(sortProperties, sortProperties.indexOf("u." + sortProperty), 0);
		if (sortProperties.indexOf(sortProperty) != 0) {
			sortProperties.remove(sortProperties.indexOf(sortProperty));
			sortProperties.addFirst(sortProperty);
		}

		Page<UserModel> usersPageable = userService.getUserPageinately(search, page, USERSIZE, startBitRange, endBitRange, sortProperties, directions);
		for (UserModel user : usersPageable.toList()) {
			if (user.getAvatar() != null)
				user.setBase64AvatarEncode(
						Base64
								.getEncoder().
								encodeToString(user.getAvatar()));
		}

		model.addAttribute("users", usersPageable.toList());
		model.addAttribute("currentPage", usersPageable.getNumber());
		model.addAttribute("totalPages", usersPageable.getTotalPages());
		model.addAttribute("directionsValue", directionsValue);
		model.addAttribute("directionsParam", directionsParam);
		model.addAttribute("search", search);
		model.addAttribute("status", status);
		model.addAttribute("sortProperty", sortProperty);
		model.addAttribute("sortProperties", sortProperties);

		return "UsersList-Admin";
	}

}
