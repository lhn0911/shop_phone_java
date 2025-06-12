package ra.edu.repository.login;

import ra.edu.entity.Admin;

public interface AdminDao {
    Admin login(String username, String password);
}
