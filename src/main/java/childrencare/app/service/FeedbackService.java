package childrencare.app.service;

import childrencare.app.model.FeedbackModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import childrencare.app.repository.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void saveGeneralFeedback(String comment,String date, byte[] image, Integer ratedStar,boolean status,  int custom_id){
        feedbackRepository.saveGeneralFeedback(comment, date, image, ratedStar, status, custom_id);
    }

    public void saveGeneralFeedback(String comment,String date, byte[] image, Integer ratedStar,boolean status){
        feedbackRepository.saveGeneralFeedback(comment, date, image, ratedStar, status);
    }

    public List<FeedbackModel> getAll(){
        List<FeedbackModel> feedbacks = feedbackRepository.findAll();
        return feedbacks;
    }

    public Page<FeedbackModel> getPaginatedFeedback(int page, int size, int sid, int star,
                                                    String contactName, String content, int status){

        Pageable pageable = PageRequest.of(page, size);
        return feedbackRepository.getAllFeedBack(sid, star, status, content, contactName, pageable);
    }

    public void changeFeedbackStatus(int status, int fid){
        feedbackRepository.changeFeedback(status, fid);
    }


}
