package childrencare.app.controller;


import childrencare.app.model.*;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationService_Service;
import childrencare.app.service.Service_service;
import childrencare.app.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ReservationListManagerController {
    @Autowired
    private ReservationService_Service reservationService_service;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private Service_service service;

    @Autowired
    private StaffService staffService;

    @RequestMapping(value = "/home/page/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
    public String getPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                           @RequestParam(name =  "key",required = false,defaultValue = "0") int key,
                           @RequestParam(name =  "sortField",required = false,defaultValue = "id") String sortField,
                           @RequestParam(name =  "sortDir",required = false,defaultValue = "asc") String sortDir){
        Page<ReservationModel> page = reservationService.listAll(pageNum,key,sortField,sortDir);
        List<ReservationModel> listInfo = page.getContent();
        model.addAttribute("listInfo",listInfo);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("key",key);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "reservationList-manager";
    }


    @GetMapping("/managerView/filter/{pageNum}")
    public String updateSlider(Model model,@PathVariable(name ="pageNum") int pageNum,
                               @Param("filterValue") boolean filterValue) {
        Page<ReservationModel> page = reservationService.filterReservation1(pageNum,filterValue);
        List<ReservationModel> listInfo = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listInfo", listInfo);
        model.addAttribute("filterValue", filterValue);
        return "reservationList-manager";
    }


    @GetMapping("/{rid}")
    public String reservationDetailStaff(@PathVariable int rid, Model model){
        ReservationModel reservationModel = reservationService.getreservationDetail(rid);
        List<ReservationServiceModel> listFind = reservationService_service.findAllByRid(rid);
        for (ReservationServiceModel item: listFind) {
            item.getService().setBase64ThumbnailEncode((item.getService().getThumbnail()));
        }
        List<ServiceModel> listServiceFind = service.findListServiceByReservationID2(rid);
        List<StaffModel> staffModelList = staffService.getAllStaff();
        model.addAttribute("listServiceFind",listServiceFind);
        model.addAttribute("reservationDetails",reservationModel);
        model.addAttribute("listFind",listFind);
        model.addAttribute("staffList",staffModelList);
        return "reservation-details-manager";
    }
    @PostMapping("/updateStatus")
    @Transactional
    public String reservationDetailStaff(@RequestParam("rid") int rid,
                                         @RequestParam("status") boolean status,
                                         Model model){
        reservationService.changeStatusReservation(status,rid);
        return "redirect:/manager/"+rid;
    }







}
