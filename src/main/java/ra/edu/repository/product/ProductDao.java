package ra.edu.repository.product;

import ra.edu.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> find_all(int pageNumber, int pageSize);
    Product find_by_id(int id);
    Product save(Product product);
    Product update(Product product);
    void delete(int id);
    List<Product> find_by_name(String name);
    List<Product> find_by_brand(String brand);
    List<Product> find_by_price_range(double minPrice, double maxPrice);
    List<Product> find_by_stock_range(int minStock, int maxStock);
    long count();
}
