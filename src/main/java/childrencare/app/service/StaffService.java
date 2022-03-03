package childrencare.app.service;

import childrencare.app.model.StaffModel;
import childrencare.app.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;

    public List<StaffModel> getAllStaff(){
        return staffRepository.findAll();
    }

    public StaffModel findStaffByEmail(String email) {
        return staffRepository.findStaffByEmail(email);
    }


}
