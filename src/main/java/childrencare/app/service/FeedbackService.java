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
        Page<FeedbackModel> feedbackModelPage = null;
        Pageable pageable = PageRequest.of(page, size);
        if(sid == 0){
            feedbackModelPage = feedbackRepository.getAllGeneralFeedback(star, status, content, contactName, pageable);
        }
        else{
            feedbackModelPage = feedbackRepository.getAllFeedBack(sid, star, status, content, contactName, pageable);
        }
        for(FeedbackModel feedback : feedbackModelPage){
            feedback.setBase64ImageEncode(feedback.getImage());
        }
        return  feedbackModelPage;
    }

    public void changeFeedbackStatus(int status, int fid){
        feedbackRepository.changeFeedback(status, fid);
    }


}
