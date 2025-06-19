package ra.edu.repository.customer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Customer;
import ra.edu.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDaoImp implements CustomerDao{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public List<Customer> findAll(int pageNumber, int pageSize) {
        Session session = null;
        List<Customer> customers = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            customers = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return customers;
    }

    @Override
    public Customer findById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Customer.class, id);
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
    public void save(Customer customer) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
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
    public void update(Customer customer) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
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
    public List<Customer> findByName(String name) {
        Session session = null;
        List<Customer> customers = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<Customer> query = session.createQuery("FROM Customer c WHERE c.name LIKE :name", Customer.class);
            query.setParameter("name", "%" + name + "%");
            customers = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return customers;
    }

    @Override
    public List<Customer> findByFilter(String name, String email, String phone, int page, int size) {
        Session session = null;
        List<Customer> customers = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("FROM Customer c WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                hql.append(" AND c.name LIKE :name");
            }
            if (email != null && !email.isEmpty()) {
                hql.append(" AND c.email LIKE :email");
            }
            if (phone != null && !phone.isEmpty()) {
                hql.append(" AND c.phone LIKE :phone");
            }
            Query<Customer> query = session.createQuery(hql.toString(), Customer.class);
            if (name != null && !name.isEmpty()) {
                query.setParameter("name", "%" + name + "%");
            }
            if (email != null && !email.isEmpty()) {
                query.setParameter("email", "%" + email + "%");
            }
            if (phone != null && !phone.isEmpty()) {
                query.setParameter("phone", "%" + phone + "%");
            }
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            customers = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return customers;
    }

    @Override
    public long countByFilter(String name, String email, String phone) {
        Session session = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT COUNT(c) FROM Customer c WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                hql.append(" AND c.name LIKE :name");
            }
            if (email != null && !email.isEmpty()) {
                hql.append(" AND c.email LIKE :email");
            }
            if (phone != null && !phone.isEmpty()) {
                hql.append(" AND c.phone LIKE :phone");
            }
            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            if (name != null && !name.isEmpty()) {
                query.setParameter("name", "%" + name + "%");
            }
            if (email != null && !email.isEmpty()) {
                query.setParameter("email", "%" + email + "%");
            }
            if (phone != null && !phone.isEmpty()) {
                query.setParameter("phone", "%" + phone + "%");
            }
            count = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    @Override
    public long count() {
        Session session = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Customer c", Long.class);
            count = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    @Override
    public boolean existsByEmail(String email) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class);
            query.setParameter("email", email);
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

    @Override
    public boolean existsByPhone(String phone) {
    Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.phone = :phone", Long.class);
            query.setParameter("phone", phone);
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
