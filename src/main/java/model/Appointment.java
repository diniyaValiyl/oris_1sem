package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Timestamp;

public class Appointment {
    private Long id;
    private Long userId;
    private Long doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private Timestamp createdAt;

    // Конструкторы
    public Appointment() {}

    public Appointment(Long id, Long userId, Long doctorId, String doctorName,
                       LocalDate appointmentDate, LocalTime appointmentTime,
                       String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Геттеры
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Сеттеры
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Билдер
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Appointment appointment = new Appointment();

        public Builder id(Long id) { appointment.id = id; return this; }
        public Builder userId(Long userId) { appointment.userId = userId; return this; }
        public Builder doctorId(Long doctorId) { appointment.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { appointment.doctorName = doctorName; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { appointment.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(LocalTime appointmentTime) { appointment.appointmentTime = appointmentTime; return this; }
        public Builder status(String status) { appointment.status = status; return this; }
        public Builder createdAt(Timestamp createdAt) { appointment.createdAt = createdAt; return this; }

        public Appointment build() { return appointment; }
    }
}