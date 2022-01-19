package childrencare.app.service;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    public List<ReservationModel> findAll() {
        return (List<ReservationModel>)repository.findAll();
    }

    public <S extends ReservationModel> S save(S entity) {
        return repository.save(entity);
    }

    public <S extends ReservationModel> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    public Optional<ReservationModel> findById(Integer integer) {
        return repository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return repository.existsById(integer);
    }

    public Iterable<ReservationModel> findAllById(Iterable<Integer> integers) {
        return repository.findAllById(integers);
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(Integer integer) {
        repository.deleteById(integer);
    }

    public void delete(ReservationModel entity) {
        repository.delete(entity);
    }

    public void deleteAllById(Iterable<? extends Integer> integers) {
        repository.deleteAllById(integers);
    }

    public void deleteAll(Iterable<? extends ReservationModel> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
