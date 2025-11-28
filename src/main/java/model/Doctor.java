package model;

import java.sql.Timestamp;

public class Doctor {
    private Long id;
    private String fullName;
    private String specialization;
    private String photoUrl;
    private String description;
    private String education;
    private Integer experience;
    private String scheduleTemplate;
    private Timestamp createdAt;

    // Конструкторы
    public Doctor() {}

    public Doctor(Long id, String fullName, String specialization, String photoUrl,
                  String description, String education, Integer experience,
                  String scheduleTemplate, Timestamp createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.photoUrl = photoUrl;
        this.description = description;
        this.education = education;
        this.experience = experience;
        this.scheduleTemplate = scheduleTemplate;
        this.createdAt = createdAt;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getSpecialization() { return specialization; }
    public String getPhotoUrl() { return photoUrl; }
    public String getDescription() { return description; }
    public String getEducation() { return education; }
    public Integer getExperience() { return experience; }
    public String getScheduleTemplate() { return scheduleTemplate; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Сеттеры
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setEducation(String education) { this.education = education; }
    public void setExperience(Integer experience) { this.experience = experience; }
    public void setScheduleTemplate(String scheduleTemplate) { this.scheduleTemplate = scheduleTemplate; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Для совместимости с существующим кодом
    public String getName() { return fullName; }
    public String getPhotoPath() { return photoUrl; }

    // Билдер
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Doctor doctor = new Doctor();

        public Builder id(Long id) { doctor.id = id; return this; }
        public Builder fullName(String fullName) { doctor.fullName = fullName; return this; }
        public Builder specialization(String specialization) { doctor.specialization = specialization; return this; }
        public Builder photoUrl(String photoUrl) { doctor.photoUrl = photoUrl; return this; }
        public Builder description(String description) { doctor.description = description; return this; }
        public Builder education(String education) { doctor.education = education; return this; }
        public Builder experience(Integer experience) { doctor.experience = experience; return this; }
        public Builder scheduleTemplate(String scheduleTemplate) { doctor.scheduleTemplate = scheduleTemplate; return this; }
        public Builder createdAt(Timestamp createdAt) { doctor.createdAt = createdAt; return this; }

        // Совместимость со старым кодом
        public Builder name(String name) { doctor.fullName = name; return this; }
        public Builder photoPath(String photoPath) { doctor.photoUrl = photoPath; return this; }

        public Doctor build() { return doctor; }
    }
}