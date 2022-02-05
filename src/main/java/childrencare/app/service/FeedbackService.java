package childrencare.app.service;

import org.springframework.stereotype.Service;

import childrencare.app.repository.FeedbackRepository;

@Service
public class FeedbackService {
	private final FeedbackRepository feedbackRepository;
	
	public FeedbackService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}
}
