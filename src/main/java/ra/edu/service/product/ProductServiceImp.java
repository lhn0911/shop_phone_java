package ra.edu.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Product;
import ra.edu.repository.product.ProductDao;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> find_all(int pageNumber, int pageSize) {
        return productDao.find_all(pageNumber, pageSize);
    }

    @Override
    public Product find_by_id(int id) {
        return productDao.find_by_id(id);
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public void delete(int id) {
        productDao.delete(id);
    }

    @Override
    public List<Product> find_by_name(String name) {
        return productDao.find_by_name(name);

    }

    @Override
    public List<Product> find_by_brand(String brand, int page, int size) {
        return productDao.find_by_brand(brand,page ,size );
    }

    @Override
    public List<Product> find_by_price_range(double minPrice, double maxPrice) {
        return productDao.find_by_price_range(minPrice, maxPrice);
    }

    @Override
    public List<Product> find_by_stock_range(int minStock, int maxStock) {
        return productDao.find_by_stock_range(minStock, maxStock);
    }

    @Override
    public long count() {
        return productDao.count();
    }

    @Override
    public long count_by_brand(String brand) {
        return productDao.count_by_brand(brand);
    }

    @Override
    public boolean existsByName(String name) {
        return productDao.existsByName(name);
    }
}
