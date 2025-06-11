package ra.edu.service.login;

import ra.edu.entity.Admin;

public interface AdminService {
    Admin login(String username, String password);
    Admin register(String username, String password);
}
