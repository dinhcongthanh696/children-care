package childrencare.app.service;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.repository.ReservationServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService_Service {
    @Autowired
    private ReservationServiceRepository reservationServiceRepository;



    public Page<ReservationServiceModel> listAll(int pageNum) {

        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, 1);

        return reservationServiceRepository.findAll(pageable);


    }

    public List<ReservationServiceModel> findAllByEmail(String email) {
        return reservationServiceRepository.findAllByEmail(email);
    }

    public List<ReservationServiceModel> getAllBookedSchedule(int reservationId){
        return reservationServiceRepository.findAllBookedSchedule(reservationId);
    }
}
