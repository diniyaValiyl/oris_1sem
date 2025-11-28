package service;

import model.User;
import java.sql.SQLException;

public interface UserService {
    User authenticateUser(String username, String password) throws SQLException;
    User getUserById(Long id) throws SQLException;
}