package ra.edu.service.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.dto.RevenueDTO;
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
    public double totalRevenue() {
        return invoiceDao.totalRevenue();
    }

    @Override
    public List<Invoice> findByFilter(Integer customerId, String status, String startDate, String endDate, Double minAmount, Double maxAmount, int page, int size) {
        return invoiceDao.findByFilter(customerId, status, startDate, endDate, minAmount, maxAmount, page, size);
    }

    @Override
    public long countByFilter(Integer customerId, String status, String startDate, String endDate, Double minAmount, Double maxAmount) {
        return invoiceDao.countByFilter(customerId, status, startDate, endDate, minAmount, maxAmount);
    }


    @Override
    public long count() {
        return invoiceDao.count();
    }

    @Override
    public List<Invoice> findByCustomerName(String name) {
        return invoiceDao.findByCustomerName(name);
    }

    @Override
    public boolean updateTotalAmount(int invoiceId) {
        return invoiceDao.updateTotalAmount(invoiceId);
    }

    @Override
    public List<Invoice> findByTime(int date, int month, int year) {
        return invoiceDao.findByTime(date, month, year);
    }

    @Override
    public List<RevenueDTO> revenueByDay() {
        return invoiceDao.revenueByDay();
    }

    @Override
    public List<RevenueDTO> revenueByMonth() {
        return invoiceDao.revenueByMonth();
    }

    @Override
    public List<RevenueDTO> revenueByYear() {
        return invoiceDao.revenueByYear();
    }
}
