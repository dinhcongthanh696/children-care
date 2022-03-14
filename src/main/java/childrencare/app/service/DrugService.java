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
	public Page<DrugModel> findAll(String title,int page, int size){
		return drugRepository.findAllBy(title,PageRequest.of(page,size));
	}
	public void addDrug(Date createDate, String drugname, Date endDate, int price, boolean status, byte[] image, String type, int quantity){
		drugRepository.addDrug(createDate, drugname, endDate, price, status, image, type, quantity);
	}
	public void updateDrug(Date createDate, String drugname, Date endDate, float price, byte[] image, String type, int quantity, int drug_id){
		drugRepository.updateDrug(createDate, drugname, endDate, price, image, type, quantity, drug_id);
	}
	public DrugModel getDrugByID(int did){
		return drugRepository.getById(did);
	}
}
