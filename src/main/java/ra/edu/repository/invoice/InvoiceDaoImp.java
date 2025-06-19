package ra.edu.repository.invoice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Invoice;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceDaoImp implements InvoiceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Invoice> findAll(int pageNumber, int pageSize) {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<Invoice> query = session.createQuery("FROM Invoice", Invoice.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            invoices = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return invoices;
    }

    @Override
    public Invoice findById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Invoice.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void save(Invoice invoice) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(invoice);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null)
                session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void update(Invoice invoice) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(invoice);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null)
                session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Invoice> findByFilter(Integer customerId, String startDate, String endDate, int page, int size) {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("FROM Invoice i WHERE 1=1");

            if (customerId != null) {
                hql.append(" AND i.customer.id = :customerId");
            }
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                hql.append(" AND i.date  BETWEEN :startDate AND :endDate");
            }

            Query<Invoice> query = session.createQuery(hql.toString(), Invoice.class);

            if (customerId != null) {
                query.setParameter("customerId", customerId);
            }
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }

            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            invoices = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return invoices;
    }

    @Override
    public long countByFilter(Integer customerId, String startDate, String endDate) {
        Session session = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT COUNT(i) FROM Invoice i WHERE 1=1");

            if (customerId != null) {
                hql.append(" AND i.customer.id = :customerId");
            }
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                hql.append(" AND i.date BETWEEN :startDate AND :endDate");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (customerId != null) {
                query.setParameter("customerId", customerId);
            }
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }

            count = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return count;
    }

    @Override
    public long count() {
        Session session = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(i) FROM Invoice i", Long.class);
            count = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return count;
    }
}
