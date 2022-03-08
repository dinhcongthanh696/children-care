package childrencare.app.controller;


import childrencare.app.model.*;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private StatusService statusService;

    @GetMapping(value = "/managerView/reservationManager/home")
    public String viewHome(Model model){
        return getPage(model,1,0,"id","asc");
    }

    @RequestMapping(value = "/home/page/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
    public String getPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                           @RequestParam(name =  "key",required = false,defaultValue = "0") int key,
                           @RequestParam(name =  "sortField",required = false,defaultValue = "id") String sortField,
                           @RequestParam(name =  "sortDir",required = false,defaultValue = "asc") String sortDir){
        Page<ReservationModel> page = reservationService.listAll(pageNum,key,sortField,sortDir);
        List<ReservationModel> listInfo = page.getContent();
        List<StatusModel> statusModels = statusService.findAll();
        model.addAttribute("statusList",statusModels);
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
    public String filterReservation(Model model,@PathVariable(name ="pageNum") int pageNum,
                               @Param("filterValue") int filterValue) {
        Page<ReservationModel> page = reservationService.filterReservation1(pageNum,filterValue);
        List<StatusModel> statusModels = statusService.findAll();
        List<ReservationModel> listInfo = page.getContent();
        model.addAttribute("statusList",statusModels);
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
        List<StatusModel> statusModels = statusService.findAll();
        for (ReservationServiceModel item: listFind) {
            item.getService().setBase64ThumbnailEncode((item.getService().getThumbnail()));
        }
        List<ServiceModel> listServiceFind = service.findListServiceByReservationID2(rid);
        List<StaffModel> staffModelList = staffService.getAllStaff();
        model.addAttribute("statusList",statusModels);
        model.addAttribute("listServiceFind",listServiceFind);
        model.addAttribute("reservationDetails",reservationModel);
        model.addAttribute("listFind",listFind);
        model.addAttribute("staffList",staffModelList);


        return "reservation-details-manager";
    }
    @PostMapping("/updateStatus")
    @Transactional
    public String reservationDetailStaff(@RequestParam("rid") int rid,
                                         @RequestParam("status") int status,
                                         Model model){
        reservationService.changeStatusReservation(status,rid);
        return "redirect:/manager/"+rid;
    }

    @PostMapping("/assignOtherStaff")
    @Transactional
    public String assignOtherStaff(
            @RequestParam("rid") int rid,
            @RequestParam(name = "staffID") Integer staffID,
            @RequestParam(name = "bookedDate") Date bookedDate,
            @RequestParam(name = "slotId") Integer slotId) {
        reservationService_service.assginOtherStaff(staffID, bookedDate, slotId);
        return "redirect:/manager/"+rid;
    }







}
