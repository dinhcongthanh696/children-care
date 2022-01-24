package childrencare.app.service;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

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

    public List<ReservationModel> findAll() {
        return repository.findAll();
    }

    @Query(value = "insert into (reservation_service reservation_id,service_id,total_person)" +
            "values (?1, ?2, ?3)", nativeQuery = true)
    public void insertReservation_Service(int rId, int sId, int total) {
        repository.insertReservation_Service(rId, sId, total);
    }

    @Query(value = "select max(reservation_id) from reservation", nativeQuery = true)
    public int idIdentity() {
        return repository.idIdentity();
    }
}
