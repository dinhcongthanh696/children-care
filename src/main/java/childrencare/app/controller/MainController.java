package childrencare.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String homePage() {
		System.out.println("Alo");
		return "index";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		System.out.println("Alo");
		return "about.html";
	}
	
	
}
