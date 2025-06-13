package ra.edu.service.product;

import ra.edu.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> find_all(int pageNumber, int pageSize);
    Product find_by_id(int id);
    void save(Product product);
    void update(Product product);
    void delete(int id);
    List<Product> find_by_name(String name);
    List<Product> find_by_brand(String brand, int page, int size);
    List<Product> find_by_price_range(double minPrice, double maxPrice);
    List<Product> find_by_stock_range(int minStock, int maxStock);
    long count();
    long count_by_brand(String brand);

}
