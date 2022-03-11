package childrencare.app.service;

import childrencare.app.model.SliderModel;
import childrencare.app.repository.SlidersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class SlidersService  {

    @Autowired
    private SlidersRepository slidersRepository;

    public void updateSlider(String back_link, byte[] img, String notes, boolean status, String title, int slide_id) {
        slidersRepository.updateSlider(back_link, img, notes, status, title, slide_id);
    }

    public void addSlider(String back_link, byte[] img, String notes, boolean status, String title) {
        slidersRepository.addSlider(back_link, img, notes, status, title);
    }



    @Modifying
    public void save(SliderModel entity) {
        slidersRepository.save(entity);
    }

    public Page<SliderModel> listAll(int pageNum, String keyword) {
        Pageable pageable = PageRequest.of(pageNum - 1, 5);
        if(keyword != null){
            return slidersRepository.findAllByField(keyword,pageable);
        }
        return slidersRepository.findAll(pageable);
    }


    public Page<SliderModel> filterByStatus(int pageNum,int status) {
        Pageable pageable1 = PageRequest.of(pageNum - 1, 5);
        if(status == -1){
            return slidersRepository.findAll(pageable1);
        }
        return slidersRepository.filterByStatus(status, pageable1);
    }


    public void changeStatusSlide(int status, int rid) {
        slidersRepository.changeStatusSlide(status, rid);
    }


    public List<SliderModel> listSliderHomepage(int status) {
        return slidersRepository.listSliderHomepage(status);
    }
}
