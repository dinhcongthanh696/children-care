package childrencare.app.controller;

import childrencare.app.model.PostModel;
import childrencare.app.service.BlogCategoryService;
import childrencare.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
            , @RequestParam(name = "page", required = false, defaultValue = "0") int page
            , @RequestParam(name = "title", required = false, defaultValue = "") String title) {

        Page<PostModel> postModels = blogService.getBlogPaging(page, BLOGSIZE, title);

        //List<PostModel> postModelList = blogService.findAllByUpdateAt();
        model.addAttribute("listPost", postModels.toList());
        model.addAttribute("listCategoryPost", blogCategoryService.findAll());
        return "blog-large";
    }

}

