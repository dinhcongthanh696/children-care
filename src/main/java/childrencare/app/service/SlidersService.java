package childrencare.app.service;

import childrencare.app.model.SliderModel;
import childrencare.app.repository.SlidersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class SlidersService  {

    @Autowired
    private SlidersRepository slidersRepository;

    public List<SliderModel> findAll() {
        return slidersRepository.findAll();
    }

    public SliderModel getById(Integer integer) {
        return slidersRepository.getById(integer);
    }
    public void updatestatusSlider(boolean status, int slide_id) {
        slidersRepository.updatestatusSlider(status, slide_id);
    }

    public void updateSlider(String back_link, MultipartFile img, String notes, boolean status, String title, int slide_id) {
        SliderModel sliderModel = new SliderModel();
        String fileName = StringUtils.cleanPath(img.getOriginalFilename());
        try{
            sliderModel.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(img.getBytes()));
        }catch (IOException ex){

        }

        slidersRepository.updateSlider(back_link,img, notes, status, title, slide_id);
    }

    public Page<SliderModel> listAll(int pageNum) {

        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, 3);

        return slidersRepository.findAll(pageable);
    }
}
