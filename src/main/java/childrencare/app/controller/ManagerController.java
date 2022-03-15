package childrencare.app.controller;

import childrencare.app.model.CustomerModel;
import childrencare.app.model.FeedbackModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.CustomerRepository;
import childrencare.app.repository.ServiceRepository;
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
    private final int SERVICESIZE = 6;
    private final int CUSTOMERSIZE = 6;
    private int POSTSIZE = 5;
    private final ServiceModelService serviceModelService;

    private final CustomerService customerService;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    private ServiceCategoryService serviceCategoryService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private BlogCategoryService blogCategoryService;

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private DrugService drugService;

    @Autowired
    private SlotService slotService;

    @Autowired
    public ManagerController(ServiceModelService serviceModelService, CustomerService customerService) {
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

    @GetMapping("/customers/{id}")
    public String toCustomerDetail(Model model, @PathVariable(name = "id") int id) {
        CustomerModel customer = customerService.getCustomerById(id);
        List<CustomerHistoryModel> customerHistoryModels = customer.getCustomerHistories();
        if (customer.getCustomer_user().getAvatar() != null) {
            customer.getCustomer_user().setBase64AvaterEncode(customer.getCustomer_user().getAvatar());
        }
        model.addAttribute("customer", customer);
        model.addAttribute("history", customerHistoryModels);
        return "CustomerDetail-Manager";

    }

    @RequestMapping(value =  "/customers",method = {RequestMethod.GET,RequestMethod.POST})
    @Transactional
    public String toCustomersList(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                  @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
                                  @RequestParam(name = "directions", required = false, defaultValue = "ascending,ascending,ascending,ascending") String directionsParam,
                                  @RequestParam(name = "sortProperty", required = false, defaultValue = "fullname") String sortProperty,
                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                  Model model) {
        int startBitRange = -1;
        int endBitRange = 2;
        switch (status) {
            case 0:
                endBitRange--;
                break;
            case 1:
                startBitRange++;
                break;
        }

        String[] directionsValue = directionsParam.split("[,]");
        Direction[] directions = new Direction[directionsValue.length];
        for (int i = 0; i < directionsValue.length; i++) {
            directions[i] = (directionsValue[i].equals("ascending")) ? Direction.ASC : Direction.DESC;
        }
        LinkedList<String> sortProperties = new LinkedList<String>(Arrays.asList("u.fullname", "u.email", "u.phone", "u.status"));
        Collections.swap(sortProperties, sortProperties.indexOf("u." + sortProperty), 0);
        if (sortProperties.indexOf("u." + sortProperty) != 0) {
            sortProperties.remove(sortProperties.indexOf("u." + sortProperty));
            sortProperties.addFirst("u." + sortProperty);
        }

        Page<CustomerModel> customersPageable = customerService.getCustomerPageinately(search, page, CUSTOMERSIZE, startBitRange, endBitRange, sortProperties, directions);
        for (CustomerModel customer : customersPageable.toList()) {
            if (customer.getCustomer_user().getAvatar() != null)
                customer.getCustomer_user().setBase64AvatarEncode(
                        Base64
                                .getEncoder().
                                encodeToString(customer.getCustomer_user().getAvatar()));
        }

        model.addAttribute("customers", customersPageable.toList());
        model.addAttribute("currentPage", customersPageable.getNumber());
        model.addAttribute("totalPages", customersPageable.getTotalPages());
        model.addAttribute("directionsValue", directionsValue);
        model.addAttribute("directionsParam", directionsParam);
        model.addAttribute("search", search);
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

    @RequestMapping(value = "/services", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchServiceListByTitle(Model model,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                           @RequestParam(name = "status", required = false, defaultValue = "") String rawStatus,
                                           @RequestParam(name = "sort", required = false, defaultValue = "title") String sort) {
        Page<ServiceModel> services;
        int startBitRangeValue = -1;
        int endBitRangeValue = 2;
        if (rawStatus.equals("true")) {
            startBitRangeValue = 0;
        } else if (rawStatus.equals("false")) {
            endBitRangeValue = 1;
        }
        services = serviceModelService.getServicesPaginated(page, SERVICESIZE, startBitRangeValue, endBitRangeValue, search, sort);

        List<ServiceCategoryModel> categories = serviceCategoryService.findAll();

        model.addAttribute("services", services.toList());
        model.addAttribute("totalPages", services.getTotalPages());
        model.addAttribute("currentPage", services.getNumber());
        model.addAttribute("search", search);
        model.addAttribute("status", rawStatus);
        model.addAttribute("categories", categories);
        model.addAttribute("sort", sort);
        return "manager-service-list";
    }

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
        	if(p.getThumbnail() != null)
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

    @RequestMapping("/postDetail")
    public String postDetail(Model model,
                             @RequestParam(name = "pid", required = false) int pid
    ) {
        PostModel post = postService.getPostDetail(pid);

        post.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(post.getThumbnail()));

        //about post
        model.addAttribute("post", post);

        //category post
        model.addAttribute("postCategoryModelList", blogCategoryService.findAll());

        //push about user
        model.addAttribute("listManagers", userService.findAllManager());
        return "post_detail_manager";
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
                                 @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(name = "serviceId", required = false, defaultValue = "-1") Integer sid,
                                 @RequestParam(name = "numberOfStar", required = false, defaultValue = "-1") Integer numberOfStar,
                                 @RequestParam(name = "status", required = false, defaultValue = "-1") Integer status,
                                 @RequestParam(name = "content", required = false, defaultValue = "") String content,
                                 @RequestParam(name = "contactName", required = false, defaultValue = "") String contactName
    ) {

        Page<FeedbackModel> feedbacks = feedbackService.getPaginatedFeedback
                (page - 1, 3, sid, numberOfStar, contactName.trim(), content.trim(), status);
        model.addAttribute("feedbacks", feedbacks.toList());


        int totalPages = feedbacks.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, page - 2);
            int end = Math.min(page + 2, totalPages - 1);
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
    @PostMapping("/updateFeedbackStatus")
    public String updateStatus(@RequestParam(name = "feedback_id") Integer fid,
                               @RequestParam(name = "status") Integer status,
                               @RequestParam(name = "page") Integer page) {
        feedbackService.changeFeedbackStatus(status, fid);
        return "redirect:/manager/feedback?page=" + page;
    }

    @RequestMapping("/drug")
    public String drugManager(Model model,
                              @RequestParam(name = "page", required = false, defaultValue = "0") Optional<Integer> page,
                              @RequestParam(name = "title", required = false) String title) {
        int currentPage = page.orElse(0);
        Page<DrugModel> pagingdrug = drugService.findAll(title,currentPage, 3);


        int totalPages = pagingdrug.getTotalPages();
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

        List<DrugModel> list = pagingdrug.toList();
        for (DrugModel d : list
        ) {
            if (d.getThumbnail() != null)
                d.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(d.getThumbnail()));
        }
        model.addAttribute("druglist", list);
        model.addAttribute("pagingPost", pagingdrug);
        model.addAttribute("title", title);
        return "drug_manager";
    }

    @PostMapping("/drug")
    @Transactional
    public String addDrug(@RequestParam(name = "drugname") String drugname,
                          @RequestParam(name = "createAt") String createAt,
                          @RequestParam(name = "endAt") String endAt,
                          @RequestParam(name = "image") MultipartFile thumbnail,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "quantity") int quantity,
                          @RequestParam(name = "type") String type) throws Exception {
        byte[] imgConvertAdd = (thumbnail == null) ? null : thumbnail.getBytes();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateCreate = formatter.parse(createAt);
        Date dateEnd = formatter.parse(endAt);


        drugService.addDrug(dateCreate, drugname, dateEnd, price, true, imgConvertAdd, type, quantity);

        return "redirect:/manager/drug";
    }

    @PostMapping("/drug/update")
    @Transactional
    public String updateDrug(

            @RequestParam(name = "drugname") String drugname,
            @RequestParam(name = "createAt") String createAt,
            @RequestParam(name = "endAt") String endAt,
            @RequestParam(name = "image") MultipartFile thumbnail,
            @RequestParam(name = "price") float price,
            @RequestParam(name = "quantity") int quantity,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "drugid") int drugId
    ) throws Exception {
        byte[] imgConvertAdd = (thumbnail == null) ? null : thumbnail.getBytes();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        Date dateCreate = null;
        Date dateEnd = null;
        if (!createAt.equals("") && !endAt.equals("")) {
            dateCreate = formatter.parse(createAt);
            dateEnd = formatter.parse(endAt);
        } else if (!createAt.equals("") && endAt.equals("")) {
            dateCreate = formatter.parse(createAt);
            dateEnd = cal.getTime();
        } else if (createAt.equals("") && !endAt.equals("")) {
            dateCreate = cal.getTime();
            dateEnd = formatter.parse(endAt);

        } else {
            dateCreate = cal.getTime();
            dateEnd = cal.getTime();
        }
        drugService.updateDrug(dateCreate, drugname, dateEnd, price, imgConvertAdd, type, quantity, drugId);
        return "redirect:/manager/drug";
    }

    @GetMapping("/drugDetail")
    public String getDrugDetail(Model model,
                                @RequestParam(name = "did", required = false) int did){
        DrugModel drugdetail = drugService.getDrugByID(did);
        if(drugdetail.getThumbnail() != null)
        drugdetail.setBase64ThumbnailEncode(Base64.getEncoder().encodeToString(drugdetail.getThumbnail()));

        model.addAttribute("drugdetail", drugdetail);
        return "drug_detail_manager";

    }

    @GetMapping("/slot")
    public String getSlotData(Model model){
        List<Slot> slots = slotService.getAllSlot();
        model.addAttribute("slots", slots);
        return "manager-slot-list";
    }
}
