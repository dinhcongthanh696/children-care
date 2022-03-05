package childrencare.app.service;


import childrencare.app.model.ServiceModel;
import childrencare.app.model.StatusModel;
import childrencare.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;


    public int findById(int id) {
        return statusRepository.findById(id);
    }

    public List<StatusModel> findAll() {
        return statusRepository.findAll();
    }
}
