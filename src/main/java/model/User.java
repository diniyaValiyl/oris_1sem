package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String gender;
    private Integer birthYear;
    private String address;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    // Конструкторы
    public User() {}

    public User(Long id, String username, String password, String fullName, String email,
                String phone, String role, String gender, Integer birthYear, String address,
                Timestamp createdAt, Timestamp lastLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
        this.birthYear = birthYear;
        this.address = address;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public Integer getBirthYear() { return birthYear; }
    public String getAddress() { return address; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getLastLogin() { return lastLogin; }

    // Сеттеры
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(String role) { this.role = role; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public void setAddress(String address) { this.address = address; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }

    // Билдер
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private User user = new User();

        public Builder id(Long id) { user.id = id; return this; }
        public Builder username(String username) { user.username = username; return this; }
        public Builder password(String password) { user.password = password; return this; }
        public Builder fullName(String fullName) { user.fullName = fullName; return this; }
        public Builder email(String email) { user.email = email; return this; }
        public Builder phone(String phone) { user.phone = phone; return this; }
        public Builder role(String role) { user.role = role; return this; }
        public Builder gender(String gender) { user.gender = gender; return this; }
        public Builder birthYear(Integer birthYear) { user.birthYear = birthYear; return this; }
        public Builder address(String address) { user.address = address; return this; }
        public Builder createdAt(Timestamp createdAt) { user.createdAt = createdAt; return this; }
        public Builder lastLogin(Timestamp lastLogin) { user.lastLogin = lastLogin; return this; }

        public User build() { return user; }
    }
}