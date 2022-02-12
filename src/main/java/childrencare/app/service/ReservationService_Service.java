package childrencare.app.service;


import childrencare.app.model.ReservationServiceModel;
import childrencare.app.repository.ReservationServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService_Service {
    @Autowired
    private ReservationServiceRepository reservationServiceRepository;


    public List<ReservationServiceModel> getCustomerReservation(String email) {
        return reservationServiceRepository.getCustomerReservation(email);
    }
}
