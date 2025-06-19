package ra.edu.repository.invoicedetail;

import ra.edu.entity.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailDao {
    void save(InvoiceDetail invoiceDetail);

    List<InvoiceDetail> findByInvoiceId(int invoiceId);

    void deleteByInvoiceId(int invoiceId);

    void update(InvoiceDetail invoiceDetail);

    void deleteById(int id);

    InvoiceDetail findById(int id);
}
