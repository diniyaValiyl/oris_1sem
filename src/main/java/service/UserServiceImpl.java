package service;

import model.User;
import dao.UserDao;
import util.HashUtil;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User authenticateUser(String username, String password) throws SQLException {
        System.out.println("🔐 Попытка аутентификации пользователя: " + username);

        User user = userDao.findUserByUsername(username);

        if (user != null) {
            System.out.println("👤 Пользователь найден в БД: " + user.getUsername());
            boolean passwordMatches = HashUtil.checkPassword(password, user.getPassword());
            System.out.println("🔑 Проверка пароля: " + (passwordMatches ? "УСПЕХ" : "НЕУДАЧА"));

            if (passwordMatches) {
                System.out.println("✅ Аутентификация успешна для пользователя: " + username);
                return user;
            }
        } else {
            System.out.println("❌ Пользователь не найден: " + username);
        }

        return null;
    }

    @Override
    public User getUserById(Long id) throws SQLException {
        return userDao.findUserById(id);
    }

    @Override
    public void registerUser(User user) throws Exception {
        // Проверка существования пользователя
        User existingUser = userDao.findUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new Exception("Пользователь с таким логином уже существует");
        }

        // Хеширование пароля
        user.setPassword(HashUtil.hashPassword(user.getPassword()));

        // Сохранение пользователя
        userDao.createUser(user);
    }

    @Override
    public void validateRegistrationData(String username, String password, String confirmPassword) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Логин обязателен для заполнения");
        }

        if (!username.matches("^[a-zA-Z0-9_]{3,50}$")) {
            throw new Exception("Логин должен содержать только латинские буквы, цифры и нижнее подчёркивание (3-50 символов)");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Пароль обязателен для заполнения");
        }

        if (password.length() < 6) {
            throw new Exception("Пароль должен содержать минимум 6 символов");
        }

        if (!password.equals(confirmPassword)) {
            throw new Exception("Пароли не совпадают");
        }
    }
}