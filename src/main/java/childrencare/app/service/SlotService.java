package childrencare.app.service;

import childrencare.app.model.Slot;
import childrencare.app.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SlotService {
    @Autowired
    SlotRepository slotRepository;

    public List<Slot> getAvailableSlot(Date date){
        return slotRepository.getAvailableSlot(date);
    }


}
