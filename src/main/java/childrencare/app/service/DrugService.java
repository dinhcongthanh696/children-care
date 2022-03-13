package childrencare.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	public List<DrugModel> findAllByStatus(boolean status){
		return drugRepository.findDrugByStatus(status);
	}
	
	public List<DrugModel> findByReservationAndService(int reservationId , int serviceId){
		return drugRepository.findDrugByReservationAndService(reservationId, serviceId);
	}
	
	public void updateDrugQuantity(DrugModel drug) {
		drugRepository.updateDrugQuantity(drug.getDrugId(), drug.getQuantity());
	}
	public Page<DrugModel> findAll(int page, int size){
		return drugRepository.findAll(PageRequest.of(page,size));
	}
	public void addDrug(Date createDate, String drugname, Date endDate, int price, boolean status, byte[] image, String type, int quantity){
		drugRepository.addDrug(createDate, drugname, endDate, price, status, image, type, quantity);
	}
}
