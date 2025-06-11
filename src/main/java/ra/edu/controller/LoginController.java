package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.AdminDTO;
import ra.edu.entity.Admin;
import ra.edu.service.login.AdminService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute("adminDTO") AdminDTO adminDTO) {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (result.hasErrors()) {
            return "login";
        }

        Admin admin = adminService.login(adminDTO.getUsername(), adminDTO.getPassword());
        if (admin != null) {
            session.setAttribute("admin", admin);
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công");
            return "redirect:/";
        } else {
            result.rejectValue("username", "invalid", "Sai tên đăng nhập hoặc mật khẩu");
            return "login";
        }
    }
    @GetMapping("/register")
    public String showRegisterForm(@ModelAttribute("adminDTO") AdminDTO adminDTO) {
        return "register";
    }
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        Admin admin = adminService.register(adminDTO.getUsername(), adminDTO.getPassword());
        if (admin != null) {
            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công");
            return "redirect:/login";
        } else {
            result.rejectValue("username", "invalid", "Tên đăng nhập đã tồn tại");
            return "register";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
