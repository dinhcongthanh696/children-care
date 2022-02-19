package childrencare.app.API;

import childrencare.app.model.Slot;
import childrencare.app.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("api-slot")
public class SlotAPI {
    //Get available doctor by Date and Service
    @Autowired
    private SlotService slotService;

    @GetMapping("/slots")
    @ResponseBody
    public List<Slot> getAvailableSlot(@RequestParam(name = "bookedDate") Date date){
        return slotService.getAvailableSlot(date);
    }

}
