package ra.edu.service.customer;

import ra.edu.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll(int pageNumber, int pageSize);
    Customer findById(int id);
    void save(Customer customer);
    void update(Customer customer);
    List<Customer> findByName(String name);
    List<Customer> findByFilter(String name, String email, String phone, int page, int size);
    long countByFilter(String name, String email, String phone);
    long count();
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
