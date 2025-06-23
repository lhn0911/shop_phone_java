package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.InvoiceDTO;
import ra.edu.dto.InvoiceDetailDTO;
import ra.edu.entity.Customer;
import ra.edu.entity.Invoice;
import ra.edu.entity.InvoiceDetail;
import ra.edu.entity.Product;
import ra.edu.service.customer.CustomerService;
import ra.edu.service.invoice.InvoiceService;
import ra.edu.service.invoicedetail.InvoiceDetailService;
import ra.edu.service.product.ProductService;
import ra.edu.utils.InvoiceStatus;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceDetailService invoiceDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listInvoices(HttpSession session,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) Integer customerId,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate,
                               @RequestParam(required = false) Double minAmount,
                               @RequestParam(required = false) Double maxAmount,
                               Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Invoice> invoices = invoiceService.findByFilter(
                customerId, status, startDate, endDate, minAmount, maxAmount, page, size);

        long totalItems = invoiceService.countByFilter(
                customerId, status, startDate, endDate, minAmount, maxAmount);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Customer> customers = customerService.findAll(1, Integer.MAX_VALUE);
        List<Product> products = productService.find_all(1, Integer.MAX_VALUE);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        List<InvoiceDetailDTO> detailDTOList = new ArrayList<>();
        for (Product p : products) {
            InvoiceDetailDTO d = new InvoiceDetailDTO();
            d.setProductId(p.getId());
            d.setQuantity(0);
            d.setUnitPrice(BigDecimal.valueOf(p.getPrice()));
            d.setTotal(BigDecimal.ZERO);
            detailDTOList.add(d);
        }
        invoiceDTO.setInvoiceDetails(detailDTOList);

        model.addAttribute("invoices", invoices);
        model.addAttribute("customers", customers);
        model.addAttribute("products", products);
        model.addAttribute("productMap", productMap);
        model.addAttribute("invoiceDTO", invoiceDTO);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", "invoice");

        model.addAttribute("customerId", customerId);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);

        return "layout";
    }


    @PostMapping("/add")
    public String addInvoice(@ModelAttribute InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        Customer customer = customerService.findById(invoiceDTO.getCustomerId());
        if (customer == null) {
            return "redirect:/invoices";
        }

        invoice.setCustomer(customer);
        invoice.setCreatedAt(new Date());
        invoice.setStatus(InvoiceStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        List<InvoiceDetail> detailList = new ArrayList<>();

        for (InvoiceDetailDTO detailDTO : invoiceDTO.getInvoiceDetails()) {
            if (detailDTO.getQuantity() <= 0) continue;

            Product product = productService.find_by_id(detailDTO.getProductId());
            if (product == null) continue;

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(detailDTO.getQuantity()));
            total = total.add(lineTotal);

            InvoiceDetail detail = new InvoiceDetail();
            detail.setInvoice(invoice);
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(unitPrice);
            detailList.add(detail);
        }

        invoice.setTotalAmount(total);
        Invoice savedInvoice = invoiceService.save(invoice);

        for (InvoiceDetail detail : detailList) {
            detail.setInvoice(savedInvoice);
            invoiceDetailService.save(detail);
        }

        return "redirect:/invoices";
    }
    @PostMapping("/edit")
    public String updateInvoiceStatus(@RequestParam("id") int id,
                                      @RequestParam("status") String status,
                                      RedirectAttributes redirectAttributes) {
        Invoice invoice = invoiceService.findById(id);
        if (invoice == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy hóa đơn cần cập nhật.");
            return "redirect:/invoices";
        }

        try {
            InvoiceStatus newStatus = InvoiceStatus.valueOf(status.toUpperCase());
            invoice.setStatus(newStatus);
            invoiceService.update(invoice);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái hóa đơn thành công.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", "Trạng thái không hợp lệ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật.");
        }

        return "redirect:/invoices";
    }




}
