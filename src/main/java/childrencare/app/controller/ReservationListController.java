package childrencare.app.controller;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.SliderModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationService_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class ReservationListController {
    @Autowired
    private ReservationService_Service reservationService_service;

    @Autowired
    private ReservationService reservationService;
    
    @RequestMapping(value = "/home/page/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
    public String getIndex(Model model, @PathVariable(name = "pageNum") int pageNum,
                           @RequestParam(name =  "key",required = false) String key,
                           @RequestParam(name =  "sortField",required = false,defaultValue = "price") String sortField,
                           @RequestParam(name =  "sortDir",required = false,defaultValue = "asc") String sortDir){
        Page<ReservationServiceModel> page = reservationService_service.listAll(pageNum,key,sortField,sortDir);
        List<ReservationServiceModel> listInfo = page.getContent();
        model.addAttribute("listInfo",listInfo);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("key",key);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "reservationList-staff";
    }
    @GetMapping("/staffview/filter/{pageNum}")
    public String updateSlider(Model model,@PathVariable(name ="pageNum") int pageNum,
                               @Param("filterValue") boolean filterValue) {
        Page<ReservationServiceModel> page = reservationService_service.filterReservation(pageNum,filterValue);
        List<ReservationServiceModel> listInfo = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listInfo", listInfo);
        model.addAttribute("filterValue", filterValue);
        return "reservationList-staff";
    }




    @GetMapping("/{rid}")
    public String reservationDetailStaff(@PathVariable int rid, Model model){
        ReservationModel reservationModel = reservationService.getreservationDetail(rid);
        List<ReservationServiceModel> listFind = reservationService_service.findAllByRid(rid);
        for (ReservationServiceModel item: listFind) {
            item.getService().setBase64ThumbnailEncode((item.getService().getThumbnail()));
        }
        model.addAttribute("reservationDetails",reservationModel);
        model.addAttribute("listFind",listFind);
        return "reservation-details-staff";
    }




}
