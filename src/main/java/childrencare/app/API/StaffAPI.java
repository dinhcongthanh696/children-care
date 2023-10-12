package childrencare.app.API;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import childrencare.app.model.DrugModel;
import childrencare.app.model.ReservationServiceDrugModel;
import childrencare.app.service.DrugService;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationServiceDrugService;
import childrencare.app.service.ReservationService_Service;
@RestController
@RequestMapping("/staff")
public class StaffAPI {
	
	@Autowired
	private DrugService drugService;
	
	
	@Autowired 
	private ReservationServiceDrugService rsdService;
	
	@Autowired
	private ReservationService_Service rsService;
	
	@Autowired
	private ReservationService rService;
	
	@Transactional
	@PostMapping(value = "/api-staff/prescription")
	public String savePrescription(@RequestParam(name = "rid") int rid , 
			@RequestParam(name = "sid") int sid , 
			@RequestParam(name = "prescriptionDrugs") String prescriptionDrugsString,
			@RequestParam(name = "drugs") String drugsString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<DrugModel> drugs = mapper.readValue(drugsString, new TypeReference<List<DrugModel>>(){});
			for(DrugModel drug : drugs) {
				drugService.updateDrugQuantity(drug);
			}
			rsdService.deletByReservationAndService(rid, sid);
			List<ReservationServiceDrugModel> prescriptionDrugs = mapper.readValue(prescriptionDrugsString, new TypeReference<List<ReservationServiceDrugModel>>(){});
			
			rsdService.addPrescriptionDrugs(prescriptionDrugs);
			rsService.updateReservationServicePrice(rid, sid);
			rService.updateReservationTotalPrice(rid);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Save Fail";
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Save Fail";
		} 
		
		return "Save Successfully";
	}
}
