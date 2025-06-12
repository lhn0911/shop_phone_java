package ra.edu.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Product;
import ra.edu.repository.product.ProductDao;

import java.util.List;

@Service
public class ProductServiceImp {
    @Autowired
    private ProductDao productDao;
    public List<Product> findAll(int pageNumber, int pageSize) {
        return productDao.find_all(pageNumber, pageSize);
    }
    public Product findById(int id) {
        return productDao.find_by_id(id);
    }
    public Product save(Product product) {
        return productDao.save(product);
    }
    public Product update(Product product) {
        return productDao.update(product);
    }
    public void delete(int id) {
        productDao.delete(id);
    }
    public List<Product> findByName(String name) {
        return productDao.find_by_name(name);
    }
    public List<Product> findByBrand(String brand) {
        return productDao.find_by_brand(brand);
    }
    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        return productDao.find_by_price_range(minPrice, maxPrice);
    }
    public List<Product> findByStockRange(int minStock, int maxStock) {
        return productDao.find_by_stock_range(minStock, maxStock);
    }
    public long count() {
        return productDao.count();
    }
}
