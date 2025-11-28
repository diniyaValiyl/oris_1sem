// UserDao.java
package dao;

import model.User;
import java.sql.SQLException;

public interface UserDao {
    void createUser(User user) throws SQLException;
    User findUserByUsername(String username) throws SQLException;
    User findUserById(Long id) throws SQLException;
}