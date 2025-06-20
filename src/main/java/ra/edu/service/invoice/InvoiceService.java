package ra.edu.service.invoice;

import ra.edu.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> findAll(int page, int size);
    Invoice findById(int id);
    Invoice save(Invoice invoice);
    void update(Invoice invoice);

    List<Invoice> findByFilter(Integer customerId, String startDate, String endDate, int page, int size);
    long countByFilter(Integer customerId, String startDate, String endDate);

    long count();

}
