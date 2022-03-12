package childrencare.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import childrencare.app.model.ReservationServiceDrugModel;
import childrencare.app.repository.ReservationServiceDrugRepository;

@Service
public class ReservationServiceDrugService {
	@Autowired
	private ReservationServiceDrugRepository rsdRepository;
	
	public List<ReservationServiceDrugModel> findByReservationAndService(int rid,int sid){
		return rsdRepository.findByReservationAndService(rid, sid);
	}
	
	public void deletByReservationAndService(int rid,int sid) {
		rsdRepository.deleteByReservationAndService(rid, sid);
	}
	
	public void addPrescriptionDrugs(List<ReservationServiceDrugModel> prescriptionDrugs) {
		for(ReservationServiceDrugModel drug : prescriptionDrugs) {
			rsdRepository.save(
					drug.getDrug().getDrugId(),
					drug.getReservation().getReservationId(),
					drug.getService().getServiceId(),
					drug.getNotes(),
					drug.getQuantity());
		}
	}
}
