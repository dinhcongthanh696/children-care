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

    public int countReservationByStatusAndDate(String statusName, Date date) {
        return repository.countReservationByStatusAndDate(statusName, date);
    }

    public List<ReservationModel> findAll() {
        return repository.findAll();
    }


    // Change Status - KVA
    public void changeStatus(Integer reservationId) {
        repository.changeStatus(reservationId);
    }

    public void createSchedule(int reservationId, int serviceId, int slotId,
                               int staff_id, Date date, double price) {
        if (staff_id == 0) {
            staff_id = staffRepository.getStaffBySlotAndDate(date, staff_id);
        }
        repository.createSchedule(reservationId, serviceId, slotId, staff_id, date, price);
    }

    public void deleteSchedule(int slotId, int staff_id, Date booked_date) {
        repository.deleteSchedule(slotId, staff_id, booked_date);
    }

    //Get Infor by RID DucAnh
    public ReservationModel getReservationByID(int rid) {
        return repository.getReservationByReservationId(rid);
    }

    public ReservationModel getReservatonInforByID(int rid) {
        ReservationModel getreserInfor = repository.getReservationByReservationId(rid);
        return getreserInfor;
    }

    public ReservationModel getreservationDetail(int reserID) {
        return repository.getreservationDetail(reserID);
    }

    public ReservationModel getreservationDetail2(int reserID) {
        return repository.getReservationByReservationId(reserID);
    }

    public Page<ReservationModel> listAll(int pageNum, int keyword, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, 6,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );
        if (keyword == 0) {
            return repository.findAll(pageable);
        }
        return repository.findReservationStaff(keyword, pageable);
    }

    public Page<ReservationModel> filterReservation1(int pageNum, int status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 6);
        if (status == -1) {
            return repository.findAll(pageable1);
        }
        return repository.filterReservationByStatus1(status, pageable1);
    }

    public Page<ReservationModel> listReservationByStaff(int pageNum, int staffID, int reservationID, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, 3,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );
        if (reservationID == 0) {
            return repository.listReservationByStaffID(staffID, pageable);
        }
        return repository.listReservationByStaff(staffID, reservationID, pageable);
    }
    
    public Page<ReservationModel> listReservationByStaff(int page , int size , int staffId , List<String> sortFields , String[] directions){
    	if(page < 0) page = 0;
    	
    	
    	return null;
    	
    }

    public Page<ReservationModel> listReservationByStaffDate(int pageNum, int staffID, Date dateFrom
            , Date dateTo, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, 3,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        return repository.listReservationByDate(staffID, dateFrom, dateTo, pageable);


    }

    public Page<ReservationModel> filterReservationByStaff(int pageNum, int staffID, int status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 3);
        if (status == -1) {
            return repository.listReservationByStaffID(staffID, pageable1);
        }
        return repository.listReservationByStaffByFilter(staffID, status, pageable1);
    }

    public void changeStatusReservation(int status, int rid) {
        repository.changeStatusReservation(status, rid);
    }


    public Page<ReservationModel> listReservationByCusID(int pageNum, int cusID) {
        Pageable pageable = PageRequest.of(pageNum - 1, 3);
        return repository.listReservationByCusID(cusID, pageable);
    }
    
    public void updateReservationTotalPrice(int rid) {
    	repository.updateReservationTotalPrice(rid);
    }


}
