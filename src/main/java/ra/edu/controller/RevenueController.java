package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.entity.Customer;
import ra.edu.service.customer.CustomerService;
import ra.edu.service.invoice.InvoiceService;
import ra.edu.service.product.ProductService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class RevenueController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProductService productService;

    @GetMapping
    public String dashboard(HttpSession session,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size,
                            Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        long totalCustomers = customerService.count();
        long totalProducts = productService.count();
        long totalInvoices = invoiceService.count();
        BigDecimal totalRevenue = BigDecimal.valueOf(invoiceService.totalRevenue());
        model.addAttribute("totalRevenue", totalRevenue);

        List<Customer> customers = customerService.findByFilter(null, null, null, 1, 5);
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalInvoices", totalInvoices);
        model.addAttribute("page", "dashboard");

        return "layout";
    }
}
