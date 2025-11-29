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
}