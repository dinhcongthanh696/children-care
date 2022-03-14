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

    public List<Slot> getAvailableSlot(Date date, Integer staff_id){
        if(staff_id == 0){
            return slotRepository.getAvailableSlot(date);
        }
        else{
            return slotRepository.getAvailableSlotWithDoctor(date, staff_id);
        }
    }

    public Slot getSlotByReservationID(int reserID){
        Slot slotByRid = slotRepository.getSlotByReservationID(reserID);
        return slotByRid;
    }

    public List<Slot> getAllSlot(){
        return slotRepository.findAll();
    }

}
