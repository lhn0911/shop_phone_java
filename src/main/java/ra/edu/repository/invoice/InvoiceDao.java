package ra.edu.repository.invoice;

import ra.edu.entity.Invoice;

import java.util.List;

public interface InvoiceDao {
    List<Invoice> findAll(int page, int size);
    Invoice findById(int id);
    void save(Invoice invoice);
    void update(Invoice invoice);
    List<Invoice> findByFilter(Integer customerId, String startDate, String endDate, int page, int size);
    long countByFilter(Integer customerId, String startDate, String endDate);
    long count();

}
