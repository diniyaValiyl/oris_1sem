package dao;

import config.DatabaseConfig;
import model.User;
import java.sql.*;

public class UserDaoImpl implements UserDao {

    @Override
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, phone, gender, birth_year, address, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getGender());
            statement.setInt(7, user.getBirthYear());
            statement.setString(8, user.getAddress());
            statement.setString(9, user.getRole());

            statement.executeUpdate();
        }
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return User.builder()
                            .id(resultSet.getLong("id"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password_hash"))
                            .fullName(resultSet.getString("full_name"))
                            .email(resultSet.getString("email"))
                            .phone(resultSet.getString("phone"))
                            .gender(resultSet.getString("gender"))
                            .birthYear(resultSet.getInt("birth_year"))
                            .address(resultSet.getString("address"))
                            .role(resultSet.getString("role"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public User findUserById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return User.builder()
                            .id(resultSet.getLong("id"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password_hash"))
                            .fullName(resultSet.getString("full_name"))
                            .email(resultSet.getString("email"))
                            .phone(resultSet.getString("phone"))
                            .gender(resultSet.getString("gender"))
                            .birthYear(resultSet.getInt("birth_year"))
                            .address(resultSet.getString("address"))
                            .role(resultSet.getString("role"))
                            .build();
                }
            }
        }
        return null;
    }
}