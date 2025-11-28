// ReviewService.java
package service;

import model.Review;
import java.sql.SQLException;
import java.util.List;

public interface ReviewService {
    void createReview(Long userId, String reviewText, Integer rating) throws SQLException;
    List<Review> getAllReviews() throws SQLException;
}
