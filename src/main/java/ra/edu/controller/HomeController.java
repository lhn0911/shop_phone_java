package ra.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/users")
    public String user() {
        return "users";
    }
    @GetMapping("/products")
    public String product() {
        return "products";
    }
    @GetMapping("/dashboard")
    public String order() {
        return "dashboard";
    }
    @GetMapping("/invoices")
    public String category() {
        return "invoices";
    }
}
