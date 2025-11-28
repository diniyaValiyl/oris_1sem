// ReviewDao.java
package dao;

import model.Review;
import java.sql.SQLException;
import java.util.List;

public interface ReviewDao {
    void createReview(Review review) throws SQLException;
    List<Review> findAllReviews() throws SQLException;
}
