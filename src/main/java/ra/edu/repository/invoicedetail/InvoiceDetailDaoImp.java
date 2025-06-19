package ra.edu.repository.invoicedetail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.InvoiceDetail;

import java.util.List;

@Repository
public class InvoiceDetailDaoImp implements InvoiceDetailDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void save(InvoiceDetail invoiceDetail) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(invoiceDetail);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
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
    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {
        Session session = null;
        List<InvoiceDetail> invoiceDetails = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM InvoiceDetail WHERE invoice.id = :invoiceId";
            invoiceDetails = session.createQuery(hql, InvoiceDetail.class)
                    .setParameter("invoiceId", invoiceId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return invoiceDetails;
    }

    @Override
    public void deleteByInvoiceId(int invoiceId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            String hql = "DELETE FROM InvoiceDetail WHERE invoice.id = :invoiceId";
            session.createQuery(hql)
                    .setParameter("invoiceId", invoiceId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
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
    public void update(InvoiceDetail invoiceDetail) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(invoiceDetail);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
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
    public void deleteById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            InvoiceDetail invoiceDetail = session.get(InvoiceDetail.class, id);
            if (invoiceDetail != null) {
                session.delete(invoiceDetail);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
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
    public InvoiceDetail findById(int id) {
        Session session = null;
        InvoiceDetail invoiceDetail = null;
        try {
            session = sessionFactory.openSession();
            invoiceDetail = session.get(InvoiceDetail.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return invoiceDetail;
    }
}
