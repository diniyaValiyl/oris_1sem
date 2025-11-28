// UserService.java
package service;

import model.User;
import java.sql.SQLException;

public interface UserService {
    void registerUser(String username, String password, String fullName, String email,
                      String phone, String gender, Integer birthYear, String address) throws Exception;
    User authenticateUser(String username, String password) throws SQLException;
    User getUserById(Long id) throws SQLException;
}