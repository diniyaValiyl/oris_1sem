package model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Review {
    private Long id;
    private Long userId;
    private String userName;
    private String reviewText;
    private Integer rating;
    private String status;
    private LocalDateTime createdDate;
    private Timestamp createdAt;

    // Конструкторы
    public Review() {}

    public Review(Long id, Long userId, String userName, String reviewText,
                  Integer rating, String status, LocalDateTime createdDate, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.reviewText = reviewText;
        this.rating = rating;
        this.status = status;
        this.createdDate = createdDate;
        this.createdAt = createdAt;
    }

    // Геттеры
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getReviewText() { return reviewText; }
    public Integer getRating() { return rating; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Сеттеры
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Билдер
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Review review = new Review();

        public Builder id(Long id) { review.id = id; return this; }
        public Builder userId(Long userId) { review.userId = userId; return this; }
        public Builder userName(String userName) { review.userName = userName; return this; }
        public Builder reviewText(String reviewText) { review.reviewText = reviewText; return this; }
        public Builder rating(Integer rating) { review.rating = rating; return this; }
        public Builder status(String status) { review.status = status; return this; }
        public Builder createdDate(LocalDateTime createdDate) { review.createdDate = createdDate; return this; }
        public Builder createdAt(Timestamp createdAt) { review.createdAt = createdAt; return this; }

        public Review build() { return review; }
    }
}