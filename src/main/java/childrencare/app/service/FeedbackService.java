package childrencare.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import childrencare.app.repository.FeedbackRepository;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void saveGeneralFeedback(String fullname, boolean gender, String email, String phone, byte[] image, Integer ratedStar, String comment){
        feedbackRepository.saveGeneralFeedback(fullname, gender, email, phone, image, ratedStar, comment);
    }


}
