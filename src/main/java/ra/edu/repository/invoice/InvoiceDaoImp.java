package ra.edu.repository.invoice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.dto.RevenueDTO;
import ra.edu.entity.Invoice;
import ra.edu.utils.InvoiceStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public List<Invoice> findByFilter(Integer customerId, String status,
                                      String startDate, String endDate,
                                      Double minAmount, Double maxAmount,
                                      int page, int size) {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("FROM Invoice i WHERE 1=1");

            if (customerId != null) {
                hql.append(" AND i.customer.id = :customerId");
            }

            if (status != null && !status.isEmpty()) {
                hql.append(" AND i.status = :status");
            }

            boolean hasDateFilter = startDate != null && !startDate.isEmpty()
                    && endDate != null && !endDate.isEmpty();
            if (hasDateFilter) {
                hql.append(" AND i.createdAt BETWEEN :startDate AND :endDate");
            }

            if (minAmount != null) {
                hql.append(" AND i.totalAmount >= :minAmount");
            }

            if (maxAmount != null) {
                hql.append(" AND i.totalAmount <= :maxAmount");
            }

            Query<Invoice> query = session.createQuery(hql.toString(), Invoice.class);

            if (customerId != null) {
                query.setParameter("customerId", customerId);
            }

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", InvoiceStatus.valueOf(status));
            }

            if (hasDateFilter) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                query.setParameter("startDate", start);
                query.setParameter("endDate", end);
            }

            if (minAmount != null) {
                query.setParameter("minAmount", java.math.BigDecimal.valueOf(minAmount));
            }

            if (maxAmount != null) {
                query.setParameter("maxAmount", java.math.BigDecimal.valueOf(maxAmount));
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
    public long countByFilter(Integer customerId, String status,
                              String startDate, String endDate,
                              Double minAmount, Double maxAmount) {
        Session session = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT COUNT(i) FROM Invoice i WHERE 1=1");

            if (customerId != null) {
                hql.append(" AND i.customer.id = :customerId");
            }

            if (status != null && !status.isEmpty()) {
                hql.append(" AND i.status = :status");
            }

            boolean hasDateFilter = startDate != null && !startDate.isEmpty()
                    && endDate != null && !endDate.isEmpty();
            if (hasDateFilter) {
                hql.append(" AND i.createdAt BETWEEN :startDate AND :endDate");
            }

            if (minAmount != null) {
                hql.append(" AND i.totalAmount >= :minAmount");
            }

            if (maxAmount != null) {
                hql.append(" AND i.totalAmount <= :maxAmount");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (customerId != null) {
                query.setParameter("customerId", customerId);
            }

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", InvoiceStatus.valueOf(status));
            }

            if (hasDateFilter) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                query.setParameter("startDate", start);
                query.setParameter("endDate", end);
            }

            if (minAmount != null) {
                query.setParameter("minAmount", java.math.BigDecimal.valueOf(minAmount));
            }

            if (maxAmount != null) {
                query.setParameter("maxAmount", java.math.BigDecimal.valueOf(maxAmount));
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

    @Override
    public List<Invoice> findByCustomerName(String name) {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Invoice i WHERE i.customer.name LIKE :name";
            Query<Invoice> query = session.createQuery(hql, Invoice.class);
            query.setParameter("name", "%" + name + "%");
            invoices = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return invoices;
    }

    @Override
    public boolean updateTotalAmount(int invoiceId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            String hql = "UPDATE Invoice i SET i.totalAmount = (SELECT SUM(id.unitPrice * id.quantity) FROM InvoiceDetail id WHERE id.invoice.id = :invoiceId) WHERE i.id = :invoiceId";
            int updatedRows = session.createQuery(hql)
                    .setParameter("invoiceId", invoiceId)
                    .executeUpdate();
            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }

    }

    @Override
    public List<Invoice> findByTime(int date, int month, int year) {
        Session session = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Invoice i WHERE DAY(i.createdAt) = :date AND MONTH(i.createdAt) = :month AND YEAR(i.createdAt) = :year";
            Query<Invoice> query = session.createQuery(hql, Invoice.class);
            query.setParameter("date", date);
            query.setParameter("month", month);
            query.setParameter("year", year);
            invoices = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return invoices;
    }

    @Override
    public List<RevenueDTO> revenueByDay() {
        Session session = null;
        List<RevenueDTO> revenueList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String hql = "SELECT new ra.edu.dto.RevenueDTO(DAY(i.createdAt), SUM(i.totalAmount)) FROM Invoice i GROUP BY DAY(i.createdAt)";
            Query<RevenueDTO> query = session.createQuery(hql, RevenueDTO.class);
            revenueList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return revenueList;
    }

    @Override
    public List<RevenueDTO> revenueByMonth() {
        Session session = null;
        List<RevenueDTO> revenueList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String hql = "SELECT new ra.edu.dto.RevenueDTO(MONTH(i.createdAt), SUM(i.totalAmount)) FROM Invoice i GROUP BY MONTH(i.createdAt)";
            Query<RevenueDTO> query = session.createQuery(hql, RevenueDTO.class);
            revenueList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return revenueList;
    }

    @Override
    public List<RevenueDTO> revenueByYear() {
        Session session = null;
        List<RevenueDTO> revenueList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            String hql = "SELECT new ra.edu.dto.RevenueDTO(YEAR(i.createdAt), SUM(i.totalAmount)) FROM Invoice i GROUP BY YEAR(i.createdAt)";
            Query<RevenueDTO> query = session.createQuery(hql, RevenueDTO.class);
            revenueList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return revenueList;
    }

    @Override
    public void updateStatus(int id, InvoiceStatus status) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            String hql = "UPDATE Invoice i SET i.status = :status WHERE i.id = :id";
            int updatedRows = session.createQuery(hql)
                    .setParameter("status", status)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            if (updatedRows == 0) {
                throw new RuntimeException("No invoice found with id: " + id);
            }
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
