package childrencare.app.controller;


import childrencare.app.model.ReservationModel;
import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.service.ReservationService;
import childrencare.app.service.Service_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private Service_service service;

    @GetMapping("/details")
    public String getHome(Model model) {
        model.addAttribute("reservations",reservationService.findAll());
        return "reservationDetails";
    }

    @GetMapping("/add/{sid}")
    public String addToCart(@PathVariable (value = "sid") int id, Model model, HttpSession session){
        Optional<ServiceModel> optinal = service.findById(id);
        ServiceModel service = null;
        if(optinal.isPresent()){
            service = optinal.get();
        }else{
            throw new RuntimeException(" Service not found for id :: " + id);
        }
        List<ServiceModel> listReservations = null;
        if(session.getAttribute("list")==null){
            listReservations = new ArrayList<>();
        }else{
            listReservations = (List<ServiceModel>) session.getAttribute("list");
        }
        boolean check =false;
        for(ServiceModel svm: listReservations){
            if(svm.getServiceId()==id){
                svm.setQuantity(svm.getQuantity()+1);
                check = true;
            }
        }
        if(check == false) {
            ServiceModel sv = new ServiceModel(service.getServiceId(), service.getThumbnail(), service.getTitle(),
                    service.getBriefInfo(),
                    service.getOriginalPrice(), service.getSalePrice(), service.getQuantity(), service.getDescription());
            listReservations.add(sv);
        }
        session.setAttribute("list",listReservations);
        model.addAttribute("list",listReservations);
        return "reservationDetails";
    }
}











