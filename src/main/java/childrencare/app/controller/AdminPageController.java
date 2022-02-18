package childrencare.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminPage")
public class AdminPageController {


    @GetMapping("/indexAdmin")
    public String indexAdmin(){
        return "index_admin";
    }
    @GetMapping("/basicFormAdmin")
    public String basicFormAdmin(){
        return "basic_form_admin";
    }
    @GetMapping("/generalAdmin")
    public String generalAdmin(){
        return "general_admin";
    }
    @GetMapping("/simpleAdmin")
    public String simpleAdmin(){
        return "simple_admin";
    }

}
