package childrencare.app.service;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.repository.ReservationServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Query(value = "select * from\n" +
            "     reservation_service rs inner join\n" +
            "     reservation r on rs.reservation_id = r.reservation_id\n" +
            "     inner join customer c on c.customer_id = r.customer_id\n" +
            "     inner join user_model u on u.email = c.customer_email\n" +
            "     inner join [service] s on rs.service_id = s.service_id\n" +
            "     inner join service_category sc on sc.service_category_id = s.service_id" +
            "     inner join slot sl on sl.slot_id = rs.slot_id\n" +
            "     inner join staff st on st.staff_id = rs.staff_id\n" +
            "     where  r.reservation_id = ?1", nativeQuery = true)
    public List<ReservationServiceModel> findAllByRid(int rid) {
        return reservationServiceRepository.findAllByRid(rid);
    }
    public Page<ReservationServiceModel> filterReservation(int pageNum,boolean status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 3);
        return reservationServiceRepository.filterReservationByStatus(status, pageable1);
    }

    public Page<ReservationServiceModel> findCustomerByEmail(int pageNum,String email) {
        Pageable pageable2 = PageRequest.of(pageNum - 1, 1);
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


}
