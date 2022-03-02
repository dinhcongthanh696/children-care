package childrencare.app.controller;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.StaffModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class ReservationListStaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private ReservationService reservationService;


    @RequestMapping(value = "/home/page/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
    public String getPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                          HttpSession session,
                          @RequestParam(name =  "key",required = false,defaultValue = "0") int key,
                          @RequestParam(name =  "sortField",required = false,defaultValue = "reservation_id") String sortField,
                          @RequestParam(name =  "sortDir",required = false,defaultValue = "asc") String sortDir){
        String emailFromSession = (String) session.getAttribute("email");
        StaffModel staffModel = staffService.findStaffByEmail(emailFromSession);
        if(staffModel != null){
            Page<ReservationModel> page =
                    reservationService.listReservationByStaff(pageNum,staffModel.getStaff_id(),key,sortField,sortDir);
            List<ReservationModel> listReserByStaff = page.getContent();
            model.addAttribute("listByStaff",listReserByStaff);
            model.addAttribute("currentPage",pageNum);
            model.addAttribute("totalPages",page.getTotalPages());
            model.addAttribute("totalItems",page.getTotalElements());
            model.addAttribute("key",key);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        }else{
            model.addAttribute("mess","Bạn không có đơn đặt lịch khám nào");
        }
        return "reservationList-staff";
    }


    @GetMapping("/staffView/filter/{pageNum}")
    public String updateSlider(Model model,@PathVariable(name ="pageNum") int pageNum,
                               @Param("filterValueByStaff") boolean filterValueByStaff,HttpSession session) {
        String emailFromSession = (String) session.getAttribute("email");
        StaffModel staffModel = staffService.findStaffByEmail(emailFromSession);
        Page<ReservationModel> page = reservationService.filterReservationByStaff(pageNum,staffModel.getStaff_id(),filterValueByStaff);
        List<ReservationModel> listByStaff = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listByStaff", listByStaff);
        model.addAttribute("filterValueByStaff", filterValueByStaff);
        return "reservationList-staff";
    }
}
