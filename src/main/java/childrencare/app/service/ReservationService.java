package childrencare.app.service;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//Nghia's code
@Service
public class ReservationService {


    @Autowired
    private ReservationRepository repository;


    public void save(ReservationModel entity) {
        repository.save(entity);
    }
    
    //Thanh's code
    public int saveReservation(ReservationModel entity) {
    	return repository.save(entity).getReservationId();
    }

    public List<ReservationModel> findAll() {
        return repository.findAll();
    }

    public void insertReservation_Service(int rId, int sId, int total) {
        repository.insertReservation_Service(rId, sId, total);
    }
    
    // Change Status - KVA
    public void changeStatus(Integer reservationId){
        repository.changeStatus(reservationId);
    }

    public void createSchedule(int reservationId,int serviceId, int slotId, String doctor, double price){
        repository.createSchedule(reservationId, serviceId, slotId, doctor, price);
    }

    public List<UserModel> getAvailableDoctor(Date date, String doctor) {
        return repository.getAvailableDoctor(date, doctor);
    }


}
