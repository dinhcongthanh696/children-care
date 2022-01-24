package childrencare.app.controller;

import childrencare.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;



    @GetMapping("/blog")
    public String blogHome(Model model) {
        model.addAttribute("listPost",blogService.findAll());
        return "blog";
    }
}
