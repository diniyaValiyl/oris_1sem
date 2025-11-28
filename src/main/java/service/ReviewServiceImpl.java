// ReviewServiceImpl.java
package service;

import dao.ReviewDao;
import model.Review;
import java.sql.SQLException;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;

    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public void createReview(Long userId, String reviewText, Integer rating) throws SQLException {
        // Валидация
        if (reviewText == null || reviewText.trim().length() < 10) {
            throw new SQLException("Отзыв должен содержать минимум 10 символов");
        }

        if (rating == null || rating < 1 || rating > 5) {
            throw new SQLException("Рейтинг должен быть от 1 до 5");
        }

        Review review = Review.builder()
                .userId(userId)
                .reviewText(reviewText)
                .rating(rating)
                .build();

        reviewDao.createReview(review);
    }

    @Override
    public List<Review> getAllReviews() throws SQLException {
        return reviewDao.findAllReviews();
    }
}