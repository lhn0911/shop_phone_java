package ra.edu.service.invoicedetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.InvoiceDetail;
import ra.edu.repository.invoicedetail.InvoiceDetailDao;

import java.util.List;

@Service
public class InvoiceDetailServiceImp implements InvoiceDetailService{
    @Autowired
    private InvoiceDetailDao invoiceDetailDao;

    @Override
    public void save(InvoiceDetail invoiceDetail) {
        invoiceDetailDao.save(invoiceDetail);
    }

    @Override
    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {
        return invoiceDetailDao.findByInvoiceId(invoiceId);
    }

    @Override
    public void deleteByInvoiceId(int invoiceId) {
        invoiceDetailDao.deleteByInvoiceId(invoiceId);
    }

    @Override
    public void update(InvoiceDetail invoiceDetail) {
        invoiceDetailDao.update(invoiceDetail);
    }

    @Override
    public void deleteById(int id) {
        invoiceDetailDao.deleteById(id);
    }

    @Override
    public InvoiceDetail findById(int id) {
        return invoiceDetailDao.findById(id);
    }
}
