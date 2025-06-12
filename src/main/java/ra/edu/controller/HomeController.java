package ra.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page", "dashboard");
        return "layout";
    }

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("page", "user");
        return "layout";
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("page", "product");
        return "layout";
    }
    @GetMapping("/invoice")
    public String invoice(Model model) {
        model.addAttribute("page", "invoice");
        return "layout";
    }
}
