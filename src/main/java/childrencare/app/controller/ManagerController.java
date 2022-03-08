package childrencare.app.controller;

import childrencare.app.model.CustomerModel;
import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.CustomerService;
import childrencare.app.service.FeedbackService;
import childrencare.app.service.ServiceModelService;
import childrencare.app.model.*;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int FEEDBACKSIZE = 6;
    
    private final int CUSTOMERSIZE = 6;
    private int POSTSIZE = 5;
    private final ServiceModelService serviceModelService;
    
    private final CustomerService customerService;
    
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    FeedbackService feedbackService;


    @Autowired
    private BlogCategoryService blogCategoryService;

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    
    @Autowired
    public ManagerController(ServiceModelService serviceModelService,CustomerService customerService) {
        this.serviceModelService = serviceModelService;
        this.customerService = customerService;
    }

    @GetMapping("/service/{id}")
    public String gotoHtml(Model model, @PathVariable(name = "id") int id) {
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services", serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
    }
    
    @GetMapping("/customers")
    @Transactional
    public String toCustomersList(@RequestParam(name = "search" , required = false , defaultValue = "") String search ,
    		@RequestParam(name = "status" , required = false , defaultValue = "-1") int status , 
    		@RequestParam(name = "directions" , required = false , defaultValue = "ascending,ascending,ascending,ascending") String directionsParam,
    		@RequestParam(name = "sortProperty" , required = false , defaultValue = "email") String sortProperty ,
    		@RequestParam(name = "page" , required = false , defaultValue = "0") int page,
    		Model model) {
    	int startBitRange = -1;
    	int endBitRange = 2;
    	switch(status) {
    		case 0 : endBitRange--;break;
    		case 1 : startBitRange++;break;
    	}
    	
    	String[] directionsValue = directionsParam.split("[,]");
    	Direction[] directions = new Direction[directionsValue.length];
    	for(int i = 0 ; i < directionsValue.length ; i++) {
    		directions[i] = (directionsValue[i].equals("ascending")) ? Direction.ASC : Direction.DESC;
    	}
    	List<String> sortProperties = Arrays.asList("u.email","u.fullname","u.phone","u.status");
    	Collections.swap(sortProperties, sortProperties.indexOf("u."+sortProperty), 0);
    	Page<CustomerModel> customersPageable = customerService.getCustomerPageinately(search, page, CUSTOMERSIZE, startBitRange, endBitRange, sortProperties, directions);
    	for(CustomerModel customer : customersPageable.toList()) {
    		if(customer.getCustomer_user().getAvatar() != null)
        		customer.getCustomer_user().setBase64AvatarEncode(
        				Base64
        				.getEncoder().
        				encodeToString(customer.getCustomer_user().getAvatar()));
    	}
    	
    	model.addAttribute("customers",customersPageable.toList());
    	model.addAttribute("currentPage", customersPageable.getNumber());
    	model.addAttribute("totalPages", customersPageable.getTotalPages());
    	model.addAttribute("directionsValue", directionsValue);
    	model.addAttribute("directionsParam", directionsParam);
    	model.addAttribute("search",search);
    	model.addAttribute("status", status);
    	model.addAttribute("sortProperty", sortProperty);
    	model.addAttribute("sortProperties", sortProperties);
    	
    	return "manager-customer-list";
    }


//    @RequestMapping(value = "/feedback", method = {RequestMethod.GET, RequestMethod.POST})
//    public String searchServiceListByTitle() {
//
//        return "manager-service-list";
//    }

    @RequestMapping("/post")
    public String reservationInfor(Model model
            , @RequestParam(name = "page", required = false, defaultValue = "0") Optional<Integer> page
            , @RequestParam(name = "type", required = false, defaultValue = "-1") String type
            , @RequestParam(name = "categoryId", required = false, defaultValue = "-1") String categoryId
            , @RequestParam(name = "titleORauthor", required = false) String title
            , @RequestParam(name = "sortBy", required = false, defaultValue = "post_id") String sortBy) {
        List<PostCategoryModel> postCategoryModelList = blogCategoryService.findAll();


        int currentPage = page.orElse(0);
        Page<PostModel> postModels = null;

        if (categoryId.equals("-1") && type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, null, null, sortBy, currentPage, POSTSIZE);
        } else if (categoryId.equals("-1") && !type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, null, type, sortBy, currentPage, POSTSIZE);
        } else if (!categoryId.equals("-1") && type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, categoryId, null, sortBy, currentPage, POSTSIZE);
        } else {
            postModels = postService.findAllAndSearch(title, categoryId, type, sortBy, currentPage, POSTSIZE);
        }

        List<PostModel> list = postModels.getContent();
        for (PostModel p : list) {
            p.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(p.getThumbnail()));
        }


        int totalPages = postModels.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages - 1);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 5;
                else if (start == 1) end = start + 5;

            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        //push about post
        model.addAttribute("listPost", list);
        model.addAttribute("postCategoryModelList", postCategoryModelList);

        //push about user
        model.addAttribute("listManagers", userService.findAllManager());


        //push all param
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pagingPost", postModels);
        model.addAttribute("type", type);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("titleORauthor", title);
        model.addAttribute("sortBy", sortBy);


        return "post_manager";
    }

    @GetMapping("/changStatus")
    @Transactional
    public String changeStatusPost(@RequestParam("pid") int rid,
                                   @RequestParam("status") String status,
                                   Model model) {
        postService.changeStatusPost((status.equals("true")) ? 1 : 0, rid);
        return "redirect:/manager/post";
    }

    @PostMapping("/post")
    @Transactional
    public String addNewPost(@RequestParam(name = "briefInfor") String briefInfor,
                             @RequestParam(name = "createAt") String createAt,
                             @RequestParam(name = "detail") String detail,
                             @RequestParam(name = "image") MultipartFile thumbnail,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "author") String email,
                             @RequestParam(name = "category") int category,
                             @RequestParam(name = "statusAdd") boolean status) throws Exception {
        byte[] imgConvertAdd = (thumbnail == null) ? null : thumbnail.getBytes();
        int postId = postService.getMaxPostId() + 1;

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        String dateInString = createAt;
        Date dateCreate = formatter.parse(dateInString);

        postService.addNewPost(postId, briefInfor, dateCreate, detail, imgConvertAdd, title, dateCreate, email, category, status);
        return "redirect:/manager/post";
    }

    @PostMapping("/post/update")
    @Transactional
    public String updatePost(

            @RequestParam(name = "briefInfor") String briefInfor,
            @RequestParam(name = "updateAt") String updateAt,
            @RequestParam(name = "detail") String detail,
            @RequestParam(name = "image") MultipartFile thumbnail,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "author") String email,
            @RequestParam(name = "category") int category,
            @RequestParam(name = "statusAdd") boolean status,
            @RequestParam(name = "postId") int postId
    ) throws Exception {
        byte[] imgConvertAdd = (thumbnail == null) ? null : thumbnail.getBytes();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar cal = Calendar.getInstance();

        Date dateUpdate = null;
        if (!updateAt.equals("")) {
            dateUpdate = formatter.parse(updateAt);
        } else {
            dateUpdate = cal.getTime();
        }


        postService.upDatePost(briefInfor, detail, imgConvertAdd, title, dateUpdate, email, category, status, postId);
        return "redirect:/manager/post";
    }


    @GetMapping("/feedback")
    public String filterFeedback(Model model,
                                 @RequestParam(name="page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(name="serviceId", required = false, defaultValue = "-1") Integer sid,
                                 @RequestParam(name="numberOfStar", required = false, defaultValue = "-1") Integer numberOfStar,
                                 @RequestParam(name="status", required = false, defaultValue = "-1") Integer status,
                                 @RequestParam(name="content", required = false, defaultValue = "") String content,
                                 @RequestParam(name="contactName", required = false, defaultValue = "") String contactName
    ){

        Page<FeedbackModel> feedbacks = feedbackService.getPaginatedFeedback
                (page - 1, 3, sid, numberOfStar, contactName.trim(), content.trim(), status);
        model.addAttribute("feedbacks", feedbacks.toList());


        int totalPages = feedbacks.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, page - 2);
            int end = Math.min(page+ 2, totalPages - 1);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 5;
                else if (start == 1) end = start + 5;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        List<ServiceModel> services = serviceModelService.getServices();
        model.addAttribute("serviceList", services);
        model.addAttribute("feedbackPage", feedbacks);

        model.addAttribute("serviceId", sid);
        model.addAttribute("numberOfStar", numberOfStar);
        model.addAttribute("status", status);
        model.addAttribute("content", content);
        model.addAttribute("contactName", contactName);

        return "manager-feedback-list";
    }

    @Transactional
    @GetMapping("/updateFeedbackStatus")
    public String updateStatus(@RequestParam(name = "feedback_id") Integer fid,
                               @RequestParam(name = "status") Integer status){
        feedbackService.changeFeedbackStatus(status, fid);
        return "redirect:/manager/feedback?page=1";
    }
}
