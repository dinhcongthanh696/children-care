package childrencare.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHome(){
        return "index";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }
}
