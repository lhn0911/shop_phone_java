package ra.edu.repository.login;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Admin;

@Repository
public class AdminDaoImp implements AdminDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Admin login(String username, String password) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Admin> query = session.createQuery(
                    "FROM Admin WHERE username = :username AND password = :password", Admin.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
