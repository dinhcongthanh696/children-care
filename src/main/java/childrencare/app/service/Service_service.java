package childrencare.app.service;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
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

    public ServiceModel getServiceById(int id){
        Optional<ServiceModel> optionalServiceModel =serviceRepository.findById(id);
        ServiceModel serviceById = null;
        if(optionalServiceModel.isPresent()){
            serviceById = optionalServiceModel.get();
            serviceById.setBase64ThumbnailEncode(serviceById.getThumbnail());
        }else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return serviceById;
    }

    public List<ServiceModel> findAllServiceByReservation(int rid){
        return serviceRepository.findListServiceByReservationID(rid);
    }
    public void updateQuantity(int quantity, int serviceId){
        serviceRepository.updateQuantity(quantity, serviceId);
    }
}
