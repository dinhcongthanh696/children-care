package childrencare.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import childrencare.app.model.DrugModel;
import childrencare.app.repository.DrugRepository;
import io.grpc.stub.StreamObserver;
import proto.DrugServiceGrpc;
import proto.Drug.DrugResoponse;
import proto.Drug.Empty;
import proto.Drug.ListDrugResponse;
import proto.DrugServiceGrpc.DrugServiceImplBase;

@GRpcService
public class DrugService extends DrugServiceImplBase{
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

	@Override
	public void findAllDrugs(Empty request, StreamObserver<ListDrugResponse> responseObserver) {
		List<DrugModel> drugModels = drugRepository.findAll();
		if(drugModels == null) return;
		List<DrugResoponse> responses = new ArrayList<>();
		for(int i = 0 ; i < drugModels.size() ; i++) {
			DrugResoponse response = DrugResoponse.newBuilder().setDrugName(drugModels.get(i).getDrugName()).build();
			responses.add(response);
		}
		ListDrugResponse listDrugResponse = ListDrugResponse.newBuilder().addAllDrugs(responses).build();
		responseObserver.onNext(listDrugResponse);
		responseObserver.onCompleted();
	}
}
