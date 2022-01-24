package childrencare.app.service;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

//Nghia's code
@Service
public class Service_service {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServiceModel> findAll() {
        List<ServiceModel> listServiceModels  =serviceRepository.findAll();
        return listServiceModels;
    }

    public Optional<ServiceModel> findById(Integer integer) {
        return serviceRepository.findById(integer);
    }

    public ServiceModel getServiceById(int id){
        Optional<ServiceModel> optionalServiceModel =serviceRepository.findById(id);
        ServiceModel serviceById = null;
        if(optionalServiceModel.isPresent()){
            serviceById =optionalServiceModel.get();
            serviceById.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(serviceById.getThumbnail()));
        }else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return serviceById;
    }

}
