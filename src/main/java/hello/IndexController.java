package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String serveIndex(Model model) {
		// System.out.println("Index constroller");
		model.addAttribute("name", "Andreas");
		return "index";
	}

}