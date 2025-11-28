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