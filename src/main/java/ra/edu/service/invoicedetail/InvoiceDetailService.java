package ra.edu.service.invoicedetail;

import ra.edu.entity.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailService {
    void save(InvoiceDetail invoiceDetail);

    List<InvoiceDetail> findByInvoiceId(int invoiceId);

    void deleteByInvoiceId(int invoiceId);

    void update(InvoiceDetail invoiceDetail);

    void deleteById(int id);

    InvoiceDetail findById(int id);
}
