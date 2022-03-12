package childrencare.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import childrencare.app.model.DrugModel;
import childrencare.app.repository.DrugRepository;

@Service
public class DrugService {
	@Autowired
	private DrugRepository drugRepository;
	
	public List<DrugModel> findAllDrugs(){
		return drugRepository.findAll();
	}
	
	public List<DrugModel> findByReservationAndService(int reservationId , int serviceId){
		return drugRepository.findDrugByReservationAndService(reservationId, serviceId);
	}
}
