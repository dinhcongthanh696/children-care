package childrencare.app.controller;

import childrencare.app.model.PostModel;
import childrencare.app.service.BlogCategoryService;
import childrencare.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BlogController {
    private BlogService blogService;
    private BlogCategoryService blogCategoryService;
    private final int BLOGSIZE = 3;

    @Autowired
    public BlogController(BlogService blogService, BlogCategoryService blogCategoryService) {
        this.blogService = blogService;
        this.blogCategoryService = blogCategoryService;
    }

    @RequestMapping(value = "/blog", method = {RequestMethod.GET, RequestMethod.POST})
    public String blogHome(Model model
            , @RequestParam(name = "page", required = false, defaultValue = "0") Optional<Integer> page
            , @RequestParam(name = "title", required = false, defaultValue = "") String title
            , @RequestParam(name = "categoryId",required = false) String categoryId) {
        int currentPage = page.orElse(0);
        Page<PostModel> postModels = null;
        if (categoryId != "" && categoryId != null) {
            postModels = blogService.getBlogPaging(currentPage, BLOGSIZE, title, Integer.parseInt(categoryId));
        } else {
            postModels = blogService.getBlogPaging(currentPage, BLOGSIZE, title);
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


        //List<PostModel> postModelList = blogService.findAllByUpdateAt();
        model.addAttribute("listPost", postModels.toList());
        model.addAttribute("listCategoryPost", blogCategoryService.findAll());
        model.addAttribute("listTop3RecentPost", blogService.findTop3RecentPost());

        model.addAttribute("titleSearch", title);
        model.addAttribute("categoryId", categoryId);

        model.addAttribute("pagingPost", postModels);
        return "blog-large";
    }

    @RequestMapping(value = "/blogDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String blogDetails(Model model
    , @RequestParam(name = "postId") int postId){
        model.addAttribute("postDetail", blogService.getPostByID(postId));
        model.addAttribute("listCategoryPost", blogCategoryService.findAll());
        model.addAttribute("listTop3RecentPost", blogService.findTop3RecentPost());
        return "blog-single";
    }
    @RequestMapping("/reservationInfor")
    public String reservationInfor(){
//asdasd
        return "reservationInfor";
    }

}

