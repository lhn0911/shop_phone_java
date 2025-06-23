package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ra.edu.entity.Invoice;
import ra.edu.entity.InvoiceDetail;
import ra.edu.service.invoice.InvoiceService;
import ra.edu.service.invoicedetail.InvoiceDetailService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceDetailController {

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Hiển thị chi tiết hóa đơn trong modal (AJAX request)
     */
    @GetMapping("/detail/{id}")
    public String getInvoiceDetailModal(@PathVariable("id") int invoiceId,
                                        HttpSession session,
                                        Model model) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("admin") == null) {
            // Trả về một fragment thông báo lỗi thay vì redirect
            model.addAttribute("errorMessage", "Bạn cần đăng nhập để xem thông tin này");
            return "error :: error-fragment";
        }

        // Lấy thông tin hóa đơn
        Invoice invoice = invoiceService.findById(invoiceId);
        if (invoice == null) {
            model.addAttribute("errorMessage", "Không tìm thấy hóa đơn");
            return "error :: error-fragment";
        }

        // Lấy danh sách chi tiết hóa đơn
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findByInvoiceId(invoiceId);

        // Tính tổng số lượng sản phẩm
        int totalQuantity = invoiceDetails.stream()
                .mapToInt(InvoiceDetail::getQuantity)
                .sum();

        // Thêm các attribute vào model
        model.addAttribute("invoice", invoice);
        model.addAttribute("invoiceDetails", invoiceDetails);
        model.addAttribute("totalQuantity", totalQuantity);

        // Trả về fragment của chi tiết hóa đơn
        return "invoicedetail :: detail-modal";
    }

    /**
     * Hiển thị chi tiết hóa đơn trong trang riêng (nếu cần)
     * Giữ lại method này để có thể truy cập trực tiếp bằng URL
     */
    @GetMapping("/view/{id}")
    public String viewInvoiceDetail(@PathVariable("id") int invoiceId,
                                    HttpSession session,
                                    Model model) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // Lấy thông tin hóa đơn
        Invoice invoice = invoiceService.findById(invoiceId);
        if (invoice == null) {
            // Có thể redirect về trang danh sách với thông báo lỗi
            return "redirect:/invoices";
        }

        // Lấy danh sách chi tiết hóa đơn
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findByInvoiceId(invoiceId);

        // Tính tổng số lượng sản phẩm
        int totalQuantity = invoiceDetails.stream()
                .mapToInt(InvoiceDetail::getQuantity)
                .sum();

        // Thêm các attribute vào model
        model.addAttribute("invoice", invoice);
        model.addAttribute("invoiceDetails", invoiceDetails);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("page", "invoice-detail");

        return "layout";
    }
}