package ra.edu.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Admin;
import ra.edu.repository.login.AdminDao;

@Service
public class AdminServiceImp implements AdminService{
    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;
        }
        return adminDao.login(username, password);
    }

}
