package childrencare.app.controller;

import childrencare.app.model.SliderModel;
import childrencare.app.service.SlidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class SliderController {

    @Autowired
    private SlidersService slidersService;


    @GetMapping("/sliderManager/page/{pageNum}")
    public String viewPage(Model model,@PathVariable(name ="pageNum") int pageNum,
                           @Param("keyword") String keyword){
        Page<SliderModel> page = slidersService.listAll(pageNum,keyword);
        List<SliderModel> slidersList = page.getContent();
        for (SliderModel slider: slidersList) {
            slider.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(slider.getImage()));
        }
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("slidersList",slidersList);
        model.addAttribute("keyword",keyword);
        return "slider_manager";
    }



    @PostMapping("/update")
    @Transactional
    public String updateSlider(@RequestParam(name = "id") int id,
                               @RequestParam(name = "imgUpdate") MultipartFile imgUpdate,
                               @RequestParam(name = "title") String title,
                               @RequestParam(name = "backlink") String backlink,
                               @RequestParam(name = "status") boolean status,
                               @RequestParam(name = "note") String note) throws Exception {

        byte[] imgConvertUpdate = (imgUpdate == null) ? null : imgUpdate.getBytes();
        slidersService.updateSlider(backlink,imgConvertUpdate,note,status,title,id);
        return "redirect:/manager/sliderManager/page/1";
    }

    @PostMapping("/addSlider")
    @Transactional
    public String addSlider(@RequestParam(name = "imgAdd") MultipartFile imgAdd,
                               @RequestParam(name = "titleAdd") String titleAdd,
                               @RequestParam(name = "backLinkAdd") String backLinkAdd,
                               @RequestParam(name = "statusAdd") boolean statusAdd,
                               @RequestParam(name = "noteAdd") String noteAdd) throws Exception {
        byte[] imgConvertAdd = (imgAdd == null) ? null : imgAdd.getBytes();
        SliderModel sliderModel = new SliderModel();
        sliderModel.setTitle(titleAdd);
        sliderModel.setBackLink(backLinkAdd);
        sliderModel.setStatus(statusAdd);
        sliderModel.setImage(imgConvertAdd);
        sliderModel.setNotes(noteAdd);
        slidersService.save(sliderModel);
        return "redirect:/manager/sliderManager/page/1";
    }




}
