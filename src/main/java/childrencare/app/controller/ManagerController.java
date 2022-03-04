package childrencare.app.controller;

import childrencare.app.model.*;
import childrencare.app.repository.ServiceRepository;
import childrencare.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private int FEEDBACKSIZE = 6;
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
    public String gotoHtml(Model model,@PathVariable(name = "id") int id){
        ServiceModel service = serviceModelService.getServiceById(id).get();
        service.setBase64ThumbnailEncode(service.getThumbnail());
        model.addAttribute("services",serviceModelService.getServices());
        model.addAttribute("service", service);
        return "ServiceDetail-Manager";
    }


    @RequestMapping(value = "/feedback", method = { RequestMethod.GET, RequestMethod.POST })
    public String searchServiceListByTitle() {

        return "manager-service-list";
    }

    @RequestMapping("/post")
    public String reservationInfor(Model model
            , @RequestParam(name = "page", required = false, defaultValue = "0") Optional<Integer> page
            , @RequestParam(name = "type", required = false, defaultValue = "-1") String type
            , @RequestParam(name = "categoryId", required = false, defaultValue = "-1") String categoryId
            , @RequestParam(name = "titleORauthor", required = false) String title) {
        List<PostCategoryModel> postCategoryModelList = blogCategoryService.findAll();


        int currentPage = page.orElse(0);
        Page<PostModel> postModels = null;

        if (categoryId.equals("-1") && type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, null, null, currentPage, 2);
        } else if (categoryId.equals("-1") && !type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, null, type, currentPage, 2);
        } else if (!categoryId.equals("-1") && type.equals("-1")) {
            postModels = postService.findAllAndSearch(title, categoryId, null, currentPage, 2);
        } else {
            postModels = postService.findAllAndSearch(title, categoryId, type, currentPage, 2);
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
        model.addAttribute("listPost", postModels.toList());
        model.addAttribute("postCategoryModelList", postCategoryModelList);

        //push about user
        model.addAttribute("listManagers", userService.findAllManager());


        //push all param
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pagingPost", postModels);
        model.addAttribute("type", type);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("titleORauthor", title);



        return "post_manager";
    }


    @GetMapping("/changStatus")
    @Transactional
    public String changeStatusPost(@RequestParam("pid") int rid,
                                   @RequestParam("status") String status,
                                   Model model){
        postService.changeStatusPost((status.equals("true"))?1:0,rid);
        return "redirect:/manager/post";
    }
}
