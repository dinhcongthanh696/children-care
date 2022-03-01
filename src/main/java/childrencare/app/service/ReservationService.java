package childrencare.app.service;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.StaffModel;
import childrencare.app.repository.ReservationRepository;
import childrencare.app.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//Nghia's code
@Service
public class ReservationService {


    @Autowired
    private StaffRepository staffRepository;

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
        if(staff_id == 0){
            staff_id = staffRepository.getStaffBySlotAndDate((java.sql.Date) date, staff_id);
        }
        repository.createSchedule(reservationId, serviceId, slotId, staff_id, date, price);
    }
    public void deleteSchedule(int slotId, int staff_id, Date booked_date){
        repository.deleteSchedule(slotId, staff_id, booked_date);
    }
    //Get Infor by RID DucAnh
    public ReservationModel getReservationByID(int rid){
        return repository.getReservationByReservationId(rid);
    }
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
    public Page<ReservationModel> listAll(int pageNum, String keyword, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, 3,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );
        if(keyword == null){
            return repository.findAll(pageable);
        }
        return repository.findReservationStaff(keyword,pageable);
    }
    public Page<ReservationModel> filterReservation1(int pageNum,boolean status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 3);
        return repository.filterReservationByStatus1(status, pageable1);
    }

    public void changeStatusReservation(boolean status, int rid) {
        repository.changeStatusReservation(status, rid);
    }


}
