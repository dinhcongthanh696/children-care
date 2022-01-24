package childrencare.app.service;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Service_service {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServiceModel> findAll() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceModel> findById(Integer integer) {
        return serviceRepository.findById(integer);
    }

    // KVA
    public void updateQuantity(int quantity, int serviceId){
        serviceRepository.updateQuantity(quantity, serviceId);
    }
}
