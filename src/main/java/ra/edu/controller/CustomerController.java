package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.CustomerDTO;
import ra.edu.entity.Customer;
import ra.edu.service.customer.CustomerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(HttpSession session,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Customer> customers = customerService.findByFilter(name, email, phone, page, size);
        long totalItems = customerService.countByFilter(name, email, phone);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("customerDTO", new CustomerDTO());
        model.addAttribute("page", "customer");

        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);

        return "layout";
    }

    @PostMapping("/add")
    public String addCustomer(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showModal", true);
            return "layout";
        }

        if (customerService.existsByEmail(customerDTO.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã tồn tại");
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showModal", true);
            return "layout";
        }

        if (customerService.existsByPhone(customerDTO.getPhone())) {
            result.rejectValue("phone", "error.phone", "Số điện thoại đã tồn tại");
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showModal", true);
            return "layout";
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customerService.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") int id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:/customers";
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress(customer.getAddress());

        model.addAttribute("customerDTO", customerDTO);
        model.addAttribute("page", "customer");
        model.addAttribute("showEditModal", true);
        return "layout";
    }

    @PostMapping("/edit")
    public String updateCustomer(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO,
                                 BindingResult result,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showEditModal", true);
            return "layout";
        }

        Customer existingCustomer = customerService.findById(customerDTO.getId());
        if (existingCustomer == null) {
            return "redirect:/customers";
        }

        if (!customerDTO.getEmail().equalsIgnoreCase(existingCustomer.getEmail())
                && customerService.existsByEmail(customerDTO.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã tồn tại");
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showEditModal", true);
            return "layout";
        }

        if (!customerDTO.getPhone().equalsIgnoreCase(existingCustomer.getPhone())
                && customerService.existsByPhone(customerDTO.getPhone())) {
            result.rejectValue("phone", "error.phone", "Số điện thoại đã tồn tại");
            prepareCustomerList(model, page, size);
            model.addAttribute("page", "customer");
            model.addAttribute("showEditModal", true);
            return "layout";
        }

        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setPhone(customerDTO.getPhone());
        existingCustomer.setAddress(customerDTO.getAddress());

        customerService.update(existingCustomer);
        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.findById(id);
        if (customer != null) {
            customer.set_deleted(!customer.is_deleted());
            customerService.update(customer);
        }
        return "redirect:/customers";
    }

    private void prepareCustomerList(Model model, int page, int size) {
        List<Customer> customers = customerService.findAll(page, size);
        long totalItems = customerService.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
    }
}