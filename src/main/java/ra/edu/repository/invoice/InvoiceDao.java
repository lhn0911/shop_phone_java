package ra.edu.repository.invoice;

import ra.edu.dto.RevenueDTO;
import ra.edu.entity.Invoice;
import ra.edu.utils.InvoiceStatus;

import java.util.List;

public interface InvoiceDao {
    List<Invoice> findAll(int page, int size);
    Invoice findById(int id);
    void save(Invoice invoice);
    void update(Invoice invoice);
    List<Invoice> findByFilter(Integer customerId, String status, String startDate, String endDate,
                               Double minAmount, Double maxAmount, int page, int size);

    long countByFilter(Integer customerId, String status, String startDate, String endDate,
                       Double minAmount, Double maxAmount);

    long count();
    List<Invoice> findByCustomerName(String name);
    boolean updateTotalAmount(int invoiceId);
    List<Invoice> findByTime(int date, int month, int year);
    List<RevenueDTO> revenueByDay();
    List<RevenueDTO> revenueByMonth();
    List<RevenueDTO> revenueByYear();
    void updateStatus(int id, InvoiceStatus status);
    double totalRevenue();
}
