package childrencare.app.controller;


import childrencare.app.model.*;
import childrencare.app.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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



    @RequestMapping(value = "/managerView/reservationManager/home", method = { RequestMethod.GET, RequestMethod.POST })
    public String getPage(Model model, @RequestParam(name = "pageNum",defaultValue = "1") int pageNum,
                           @RequestParam(name =  "key",required = false,defaultValue = "0") int key,
                          @RequestParam(name =  "filterValue",required = false,defaultValue = "0") int filterValue,
                           @RequestParam(name =  "sortField",required = false,defaultValue = "id") String sortField,
                           @RequestParam(name =  "sortDir",required = false,defaultValue = "asc") String sortDir){
        Page<ReservationModel> page = reservationService.listAll(pageNum,key,filterValue,sortField,sortDir);
        List<ReservationModel> listInfo = page.getContent();
        List<StatusModel> statusModels = statusService.findAll();
        model.addAttribute("filterValue", filterValue);
        model.addAttribute("statusList",statusModels);
        model.addAttribute("listInfo",listInfo);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("key",key);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        List<String> sevenLastDays = reservationService.lastSevenDateReservation();
        List<Integer> listTotalPriceByDate = new ArrayList<>();
        for (int i =0 ; i< sevenLastDays.size(); i++){
            int totalPriceByDate = reservationService.totalPricelastSevenDateReservation(sevenLastDays.get(i));
            listTotalPriceByDate.add(totalPriceByDate);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        model.addAttribute("totalPriceByDate",listTotalPriceByDate);
        try {
            model.addAttribute("sevenLastDays", objectMapper.writeValueAsString(sevenLastDays));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "reservationList-manager";
    }



    @RequestMapping(value = "/managerView/reservationManager/detailsReservation", method = { RequestMethod.GET, RequestMethod.POST })
    public String reservationDetailStaff(@RequestParam(name =  "rid") int rid,
                                         Model model){
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
    @PostMapping("/reservation/updateStatus")
    @Transactional
    public String reservationDetailStaff(@RequestParam("rid") int rid,
                                         @RequestParam("status") int status,
                                         Model model){
        reservationService.changeStatusReservation(status,rid);
        return "redirect:/manager/managerView/reservationManager/detailsReservation?rid="+rid;
    }

//    @PostMapping("/assignOtherStaff")
//    @Transactional
//    public String assignOtherStaff(
//            @RequestParam(name = "rid") int rid,
//            @RequestParam(name = "staffID") int staffID,
//            @RequestParam(name = "bookedDate") Date bookedDate,
//            @RequestParam(name = "slotId") int slotId) {
//        reservationService_service.assginOtherStaff(staffID, bookedDate, slotId);
//        return "redirect:/manager/managerView/reservationManager/detailsReservation?rid="+rid;
//    }


    @GetMapping("/managerView/reservationManager/home/export")
    public void exportToPDF(HttpServletResponse response)throws DocumentException, IOException {
        response.setContentType("application/pdf");
        String headerKey ="Content-Disposition";
        String headerValue = "attachment;filename=Manager-List-AllReservation.pdf";
        response.setHeader(headerKey,headerValue);
        List<ReservationModel> listReservations = reservationService.findAll();
        ReservationPDFExporter exporter = new ReservationPDFExporter(listReservations);
        exporter.export(response);


    }





}
