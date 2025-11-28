package service;

import dao.UserDao;
import model.User;
import util.HashUtil;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void registerUser(String username, String password, String fullName, String email,
                             String phone, String gender, Integer birthYear, String address) throws Exception {

        // Проверка логина
        if (username == null || username.trim().length() < 3) {
            throw new Exception("Логин должен содержать минимум 3 символа");
        }

        // Проверка пароля
        if (password == null || password.length() < 6) {
            throw new Exception("Пароль должен содержать минимум 6 символов");
        }

        // Проверка существующего пользователя
        User existingUser = userDao.findUserByUsername(username);
        if (existingUser != null) {
            throw new Exception("Пользователь с таким именем уже существует");
        }

        // Проверка email
        if (email != null && !email.isEmpty()) {
            User existingEmailUser = userDao.findUserByEmail(email);
            if (existingEmailUser != null) {
                throw new Exception("Пользователь с таким email уже существует");
            }
        }

        // Хэширование пароля
        String hashedPassword = HashUtil.hashPassword(password);

        // Создание пользователя
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .gender(gender)
                .birthYear(birthYear)
                .address(address)
                .role("PATIENT")
                .build();

        // Сохранение в базу
        userDao.createUser(user);
    }

    @Override
    public User authenticateUser(String username, String password) throws SQLException {
        User user = userDao.findUserByUsername(username);
        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserById(Long id) throws SQLException {
        return userDao.findUserById(id);
    }
}