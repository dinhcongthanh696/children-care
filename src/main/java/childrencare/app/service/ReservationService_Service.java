package childrencare.app.service;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.repository.ReservationServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ReservationService_Service {
    @Autowired
    private ReservationServiceRepository reservationServiceRepository;

    public Page<ReservationServiceModel> listAll(int pageNum) {
        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, 1);
        return reservationServiceRepository.findAll(pageable);

    }

    public List<ReservationServiceModel> getAllBookedSchedule(int reservationId){
        return reservationServiceRepository.findAllBookedSchedule(reservationId);
    }

    public List<ReservationServiceModel> findAllByRid(int rid) {
        return reservationServiceRepository.findAllByRid(rid);
    }
    public Page<ReservationServiceModel> filterReservation(int pageNum,int status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 3);
        return reservationServiceRepository.filterReservationByStatus(status, pageable1);
    }

    public Page<ReservationServiceModel> findCustomerByEmail(int pageNum,String email) {
        Pageable pageable2 = PageRequest.of(pageNum - 1, 3);
        return reservationServiceRepository.findCustomerByEmail(email,pageable2);
    }

    public Page<ReservationServiceModel> listAll(int pageNum,String keyword, String sortField, String sortDir) {

        Pageable pageable = PageRequest.of(pageNum - 1, 3,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );
        if(keyword == null){
            return reservationServiceRepository.findAll(pageable);
        }
        return reservationServiceRepository.findAllReserInfo(keyword,pageable);
    }
    public void deleteByRidAndSidAndSlotid(int rid, int sid, int slotid, String date){
        reservationServiceRepository.deleteInListServiceByReservationAndServiceAndSlotid(rid,sid,slotid,date);
    }


    public List<ReservationServiceModel> findAllServiceByStaffAndRid(int staffID, int reservation_id) {
        return reservationServiceRepository.findAllServiceByStaffAndRid(staffID, reservation_id);
    }
    
    public Page<ReservationServiceModel> findReservationServiceByStaffAndServiceAndDrugs( int page , 
    		int size , int staffId , int serviceId , List<Integer> drugIds
    ){
    	if(page < 0) page = 0;
    	Page<ReservationServiceModel> reservationServicesPageable = 
    			reservationServiceRepository.listReservationByStaffAndServiceAndDrugs(staffId, serviceId, drugIds,drugIds.size(), PageRequest.of(page, size));
    	if(reservationServicesPageable.getTotalPages() > 0 && 
    		page >= reservationServicesPageable.getTotalPages()	) {
    		page = reservationServicesPageable.getTotalPages() - 1;
    		reservationServicesPageable = 
        			reservationServiceRepository.listReservationByStaffAndServiceAndDrugs(staffId, serviceId, drugIds ,drugIds.size(), PageRequest.of(page, size));
    	}
    	return reservationServicesPageable;
    }


    public void assginOtherStaff(int staffID, Date booked_date, int slot_id) {
        reservationServiceRepository.assginOtherStaff(staffID,booked_date, slot_id);
    }
    
    public void updateReservationServicePrice(int rid,int sid) {
    	reservationServiceRepository.updateReservationServicePrice(rid, sid);
    }

    public float getSumService(int reserId){
        return reservationServiceRepository.getSumService(reserId);
    }


    public ReservationServiceModel checkStaffEmptyDateStaff(Date bookedDate, int slotID, int staffID) {
        return reservationServiceRepository.checkStaffEmptyDateStaff(bookedDate, slotID, staffID);
    }
}
