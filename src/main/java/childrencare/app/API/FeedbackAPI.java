package childrencare.app.API;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import childrencare.app.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import childrencare.app.CaptchaGenerator;
import childrencare.app.model.FeedbackModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.FeedbackRepository;
import childrencare.app.repository.ReservationRepository;
import childrencare.app.repository.UserRepository;

@RestController
@RequestMapping(path = "/api-feedback")
public class FeedbackAPI {
	private final JavaMailSender mailSender;
	private final UserRepository userRepository;
	private final FeedbackRepository feedbackRepository;
	private final ReservationRepository reservationRepository;
	private Map<String,String> usersCaptcha;
	private final FeedbackService feedbackService;
	
	@Autowired
	public FeedbackAPI(JavaMailSender mailSender,UserRepository userRepository,
					   FeedbackRepository feedbackRepository,
					   ReservationRepository reservationRepository,
					   FeedbackService feedbackService) {
		this.mailSender = mailSender;
		this.userRepository = userRepository;
		this.feedbackRepository = feedbackRepository;
		this.reservationRepository = reservationRepository;
		this.usersCaptcha = new HashMap<String,String>();
		this.feedbackService = feedbackService;
	}
	
	@PostMapping("/verify-captcha")
	public String verifyUserContactCaptcha(HttpSession session , @RequestParam(name = "email") String email , 
			@RequestParam(name = "captcha") String userInputcaptcha) {
		if(session.getAttribute("user") != null) {
			return "user exsist";
		}
		
		if(!usersCaptcha.get(email).equals(userInputcaptcha)) {
			return "fail";
		}
		UserModel user = userRepository.findByEmail(email);
		if(user == null) {
			return "not exsist";
		}
		usersCaptcha.remove(email);
		session.setAttribute("user", user);
		if(user.getAvatar() != null) user.setBase64AvatarEncode(Base64.getEncoder().encodeToString(user.getAvatar()));
		return "success";
	}
	
	@PostMapping("/generate-captcha")
	public String generateUserContactCaptcha(HttpSession session ,@RequestParam(name = "email") String email) {
		if(session.getAttribute("user") != null) {
			return "verified";
		}
		// 1. Generate captcha
		
		String captcha = CaptchaGenerator.generateCaptchaCode();
		usersCaptcha.put(email, captcha);
		
		// if user does not have account
		// 2. identify user email
		JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
		String from = mailSenderImpl.getUsername();
		String to = email;
		String subject = "Email identifycation from Children Care";
		MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
		try {
			mimeMessage.setFrom(from);
			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			mimeMessage.setSubject(subject);
			
			MimeBodyPart contentPart = new MimeBodyPart();
			String content = "<h1> Hello "+ email +" , your captcha is : "+ captcha +" </h1>" ;
			contentPart.setContent(content, "text/html; charset=utf-8");
			
			MimeBodyPart referencePart = new MimeBodyPart();
			String reference = "<a href='http://localhost:8080/ChildrenCare/'> Children Care's Page</a>";
			referencePart.setContent(reference,"text/html; charset=utf-8");
			
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(contentPart);
			multiPart.addBodyPart(referencePart);
			
			mimeMessage.setContent(multiPart);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return captcha;
	}
	
	@GetMapping("/identify-user-email")
	public String identifyUserEmail(@RequestParam(name = "email") String email) {
		UserModel user = userRepository.findByEmail(email);
		if(user == null) {
			return "not exsist";
		}
		return "exist";
	}
	
	@PostMapping("/save-feedback")
	@Transactional
	public String saveUserFeedback(@RequestParam(name = "imageFeedback" , required = false) MultipartFile image , 
			@RequestParam(name = "commentFeedback") String comment , 
			@RequestParam(name = "serviceId") Integer serviceId  ,
			@RequestParam(name = "ratedStar") Integer ratedStar ,
			HttpSession session
			) {
		UserModel user = (UserModel) session.getAttribute("user");
		if(user == null) return "We don't know you,You need to give your contact or login!!!";
		if(serviceId == -1) return "You don't have chosen any service to feedback!!";
		try {
			byte[] imageBinaryData = (image == null) ? null : image.getBytes();
			if(user.getCustomer() != null)
			feedbackRepository.saveOnlyFeedback(comment, imageBinaryData, ratedStar, false, serviceId , user.getCustomer().getCustomer_id()); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	@GetMapping("/feedback")
	public Page<FeedbackModel> filterFeedback(
			@RequestParam(name="page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name="serviceId", required = false, defaultValue = "-1") Integer sid,
			@RequestParam(name="numberOfStar", required = false, defaultValue = "-1") Integer numberOfStar,
			@RequestParam(name="status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(name="content", required = false, defaultValue = "") String content,
			@RequestParam(name="contactName", required = false, defaultValue = "") String contactName
	) {

		Page<FeedbackModel> feedbacks = feedbackService.getPaginatedFeedback
				(page, 3, sid, numberOfStar, contactName, content, status);
		return feedbacks;
	}
}
