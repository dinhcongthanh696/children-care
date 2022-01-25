package childrencare.app.controller;

import childrencare.app.service.BlogCategoryService;
import childrencare.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {
    private BlogService blogService;
    private BlogCategoryService blogCategoryService;
    private final int BLOGSIZE = 3;

    @Autowired
    public BlogController(BlogService blogService,BlogCategoryService blogCategoryService) {
        this.blogService = blogService;
        this.blogCategoryService = blogCategoryService;
    }

    @GetMapping("/blog")
    public String blogHome(Model model) {
        model.addAttribute("listPost",blogService.findAll());
        model.addAttribute("listCategoryPost",blogCategoryService.findAll());
        return "blog-large";
    }
}
