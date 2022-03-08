package childrencare.app.controller;

import childrencare.app.model.*;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int FEEDBACKSIZE = 6;
    private int POSTSIZE = 5;
    private final ServiceModelService serviceModelService;
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

    public ManagerController(ServiceModelService serviceModelService) {
        this.serviceModelService = serviceModelService;
    }

    @GetMapping("/service/{id}")
    public String gotoHtml(Model model, @PathVariable(name = "id") int id) {
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services", serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
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
