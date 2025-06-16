package ra.edu.repository.product;

import ra.edu.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> find_all(int pageNumber, int pageSize);
    Product find_by_id(int id);
    void save(Product product);
    void update(Product product);
    List<Product> find_by_name(String name);
    List<Product> findByFilter(String brand, Double minPrice, Double maxPrice,
                               Integer minStock, Integer maxStock, int page, int size);
    long countByFilter(String brand, Double minPrice, Double maxPrice,
                       Integer minStock, Integer maxStock);

    long count();
    long count_by_brand(String brand);
    boolean existsByName(String name);


}
