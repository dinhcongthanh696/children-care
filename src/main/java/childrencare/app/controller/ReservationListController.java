package childrencare.app.controller;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.ReservationService_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class ReservationListController {
    @Autowired
    private ReservationService_Service reservationService_service;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/home/page/{pageNum}")
    public String getIndex(Model model, @PathVariable(name = "pageNum") int pageNum,
                           @Param("keyword") String keyword){
        Page<ReservationServiceModel> page = reservationService_service.findAllReser(pageNum,keyword);
        List<ReservationServiceModel> listInfo = page.getContent();
        model.addAttribute("listInfo",listInfo);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("keyword",keyword);
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
