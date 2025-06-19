package ra.edu.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Customer;
import ra.edu.repository.customer.CustomerDao;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Override
    public List<Customer> findAll(int pageNumber, int pageSize) {
        return customerDao.findAll(pageNumber, pageSize);
    }

    @Override
    public Customer findById(int id) {
        return customerDao.findById(id);
    }

    @Override
    public void save(Customer customer) {
        customerDao.save(customer);
    }

    @Override
    public void update(Customer customer) {
        customerDao.update(customer);
    }

    @Override
    public List<Customer> findByName(String name) {
        return customerDao.findByName(name);
    }

    @Override
    public List<Customer> findByFilter(String name, String email, String phone, int page, int size) {
        return customerDao.findByFilter(name, email, phone, page, size);
    }

    @Override
    public long countByFilter(String name, String email, String phone) {
        return customerDao.countByFilter(name, email, phone);
    }

    @Override
    public long count() {
        return customerDao.count();
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerDao.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return customerDao.existsByPhone(phone);
    }
}
