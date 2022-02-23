package childrencare.app.service;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
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
    
    public int countReservationByStatus(int status) {
    	return repository.countReservationByStatus(status);
    }
    
    public int countReservationByStatusAndDate(int status , Date date) {
    	return repository.countReservationByStatusAndDate(status, date);
    }

    public List<ReservationModel> findAll() {
        return repository.findAll();
    }

    
    // Change Status - KVA
    public void changeStatus(Integer reservationId){
        repository.changeStatus(reservationId);
    }

    public void createSchedule(int reservationId,int serviceId, int slotId,
                               int staff_id, Date date, double price){
        repository.createSchedule(reservationId, serviceId, slotId, staff_id, date, price);
    }

    public void deleteSchedule(int slotId, int staff_id, Date booked_date){
        repository.deleteSchedule(slotId, staff_id, booked_date);
    }
    //Get Infor by RID DucAnh
    public ReservationModel getReservatonInforByID(int rid){
        ReservationModel getreserInfor = repository.getReservationModelByReservationId(rid);
        return getreserInfor;
    }

    public ReservationModel getreservationDetail(int reserID) {
        return repository.getreservationDetail(reserID);
    }

    
    public ReservationModel getreservationDetail2(int reserID) {
        return repository.getReservationByReservationId(reserID);
    }

}
