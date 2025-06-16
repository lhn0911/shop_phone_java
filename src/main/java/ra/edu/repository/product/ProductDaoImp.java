package ra.edu.repository.product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImp implements ProductDao{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public List<Product> find_all(int pageNumber, int pageSize) {
        Session session = null;
        List<Product> productList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            productList = query.getResultList();
            return productList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return productList;
    }

    @Override
    public Product find_by_id(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void save(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void delete(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.delete(product);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> find_by_name(String name) {
        Session session = null;
        List<Product> productList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<Product> query = session.createQuery("FROM Product WHERE name LIKE :name", Product.class);
            query.setParameter("name", "%" + name + "%");
            productList = query.getResultList();
            return productList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return productList;
    }

    @Override
    public List<Product> findByFilter(String brand, Double minPrice, Double maxPrice, Integer minStock, Integer maxStock, int page, int size) {
        Session session = null;
        List<Product> productList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("FROM Product WHERE 1=1");
            if (brand != null && !brand.isEmpty()) {
                hql.append(" AND brand LIKE :brand");
            }
            if (minPrice != null) {
                hql.append(" AND price >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND price <= :maxPrice");
            }
            if (minStock != null) {
                hql.append(" AND stock >= :minStock");
            }
            if (maxStock != null) {
                hql.append(" AND stock <= :maxStock");
            }

            Query<Product> query = session.createQuery(hql.toString(), Product.class);
            if (brand != null && !brand.isEmpty()) {
                query.setParameter("brand", "%" + brand + "%");
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }
            if (minStock != null) {
                query.setParameter("minStock", minStock);
            }
            if (maxStock != null) {
                query.setParameter("maxStock", maxStock);
            }

            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            productList = query.getResultList();
            return productList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return productList;
    }

    @Override
    public long countByFilter(String brand, Double minPrice, Double maxPrice, Integer minStock, Integer maxStock) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1");
            if (brand != null && !brand.isEmpty()) {
                hql.append(" AND p.brand LIKE :brand");
            }
            if (minPrice != null) {
                hql.append(" AND p.price >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND p.price <= :maxPrice");
            }
            if (minStock != null) {
                hql.append(" AND p.stock >= :minStock");
            }
            if (maxStock != null) {
                hql.append(" AND p.stock <= :maxStock");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            if (brand != null && !brand.isEmpty()) {
                query.setParameter("brand", "%" + brand + "%");
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }
            if (minStock != null) {
                query.setParameter("minStock", minStock);
            }
            if (maxStock != null) {
                query.setParameter("maxStock", maxStock);
            }

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

//    @Override
//    public List<Product> find_by_brand(String brand, int page, int size) {
//        Session session = null;
//        List<Product> productList = new ArrayList<>();
//        try {
//            session = sessionFactory.openSession();
//            Query<Product> query = session.createQuery("FROM Product WHERE brand LIKE :brand", Product.class);
//            query.setParameter("brand", "%" + brand + "%");
//            productList = query.getResultList();
//            return productList;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return productList;
//    }

//    @Override
//    public List<Product> find_by_price_range(double minPrice, double maxPrice) {
//        Session session = null;
//        List<Product> productList = new ArrayList<>();
//        try {
//            session = sessionFactory.openSession();
//            Query<Product> query = session.createQuery("FROM Product WHERE price BETWEEN :minPrice AND :maxPrice", Product.class);
//            query.setParameter("minPrice", minPrice);
//            query.setParameter("maxPrice", maxPrice);
//            productList = query.getResultList();
//            return productList;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return productList;
//    }

//    @Override
//    public List<Product> find_by_stock_range(int minStock, int maxStock) {
//    Session session = null;
//        List<Product> productList = new ArrayList<>();
//        try {
//            session = sessionFactory.openSession();
//            Query<Product> query = session.createQuery("FROM Product WHERE stock BETWEEN :minStock AND :maxStock", Product.class);
//            query.setParameter("minStock", minStock);
//            query.setParameter("maxStock", maxStock);
//            productList = query.getResultList();
//            return productList;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return productList;
//    }

    @Override
    public long count() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public long count_by_brand(String brand) {
    Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Product p WHERE p.brand LIKE :brand", Long.class);
            query.setParameter("brand", "%" + brand + "%");
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean existsByName(String name) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Product p WHERE p.name = :name", Long.class);
            query.setParameter("name", name);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
