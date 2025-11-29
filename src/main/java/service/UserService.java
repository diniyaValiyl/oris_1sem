package service;

import model.User;
import java.sql.SQLException;

public interface UserService {

    // Существующие методы
    User authenticateUser(String username, String password) throws SQLException;
    User getUserById(Long id) throws SQLException;

    // Новые методы для регистрации
    void registerUser(User user) throws Exception;
    void validateRegistrationData(String username, String password, String confirmPassword) throws Exception;
}