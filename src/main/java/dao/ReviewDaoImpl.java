
// ReviewDaoImpl.java
package dao;

import config.DatabaseConfig;
import model.Review;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements ReviewDao {

    @Override
    public void createReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, content, rating) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, review.getUserId());
            statement.setString(2, review.getReviewText());
            statement.setInt(3, review.getRating());

            statement.executeUpdate();
        }
    }

    @Override
    public List<Review> findAllReviews() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.status = 'PUBLISHED' " +
                "ORDER BY r.created_at DESC";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reviews.add(Review.builder()
                        .id(resultSet.getLong("id"))
                        .userId(resultSet.getLong("user_id"))
                        .userName(resultSet.getString("full_name"))
                        .reviewText(resultSet.getString("content"))
                        .rating(resultSet.getInt("rating"))
                        .createdDate(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        }
        return reviews;
    }
}