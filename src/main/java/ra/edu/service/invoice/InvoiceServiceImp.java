package ra.edu.service.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Invoice;
import ra.edu.repository.invoice.InvoiceDao;
import ra.edu.utils.InvoiceStatus;

import java.util.List;

@Service
public class InvoiceServiceImp implements InvoiceService{
    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public List<Invoice> findAll(int page, int size) {
        return invoiceDao.findAll(page, size);
    }

    @Override
    public Invoice findById(int id) {
        return invoiceDao.findById(id);
    }

    @Override
    public Invoice save(Invoice invoice) {
        invoiceDao.save(invoice);
        return invoice;
    }

    @Override
    public void update(Invoice invoice) {
        invoiceDao.update(invoice);
    }

    @Override
    public void updateStatus(int id, InvoiceStatus status) {
        invoiceDao.updateStatus(id, status);
    }

    @Override
    public List<Invoice> findByFilter(Integer customerId, String startDate, String endDate, int page, int size) {
        return invoiceDao.findByFilter(customerId, startDate, endDate, page, size);
    }

    @Override
    public long countByFilter(Integer customerId, String startDate, String endDate) {
        return invoiceDao.countByFilter(customerId, startDate, endDate);
    }

    @Override
    public long count() {
        return invoiceDao.count();
    }
}
