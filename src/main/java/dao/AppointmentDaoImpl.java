package dao;

import config.DatabaseConfig;
import model.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public void createAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (user_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, appointment.getUserId());
            statement.setLong(2, appointment.getDoctorId());
            statement.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            statement.setString(5, "SCHEDULED");

            statement.executeUpdate();
        }
    }

    @Override
    public List<Appointment> findAppointmentsByUserId(Long userId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, d.full_name as doctor_name FROM appointments a " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.user_id = ? AND a.status = 'SCHEDULED' " +
                "ORDER BY a.appointment_date, a.appointment_time";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(Appointment.builder()
                            .id(resultSet.getLong("id"))
                            .userId(resultSet.getLong("user_id"))
                            .doctorId(resultSet.getLong("doctor_id"))
                            .doctorName(resultSet.getString("doctor_name"))
                            .appointmentDate(resultSet.getDate("appointment_date").toLocalDate())
                            .appointmentTime(resultSet.getTime("appointment_time").toLocalTime())
                            .status(resultSet.getString("status"))
                            .build());
                }
            }
        }
        return appointments;
    }

    @Override
    public List<Appointment> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND status = 'SCHEDULED'";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, doctorId);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(Appointment.builder()
                            .id(resultSet.getLong("id"))
                            .userId(resultSet.getLong("user_id"))
                            .doctorId(resultSet.getLong("doctor_id"))
                            .appointmentDate(resultSet.getDate("appointment_date").toLocalDate())
                            .appointmentTime(resultSet.getTime("appointment_time").toLocalTime())
                            .status(resultSet.getString("status"))
                            .build());
                }
            }
        }
        return appointments;
    }

    @Override
    public boolean isTimeSlotAvailable(Long doctorId, LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status != 'CANCELLED'";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, doctorId);
            statement.setDate(2, Date.valueOf(date));
            statement.setTime(3, Time.valueOf(time));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    @Override
    public List<Appointment> findAppointmentsByDoctorId(Long doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND status = 'SCHEDULED'";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, doctorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(Appointment.builder()
                            .id(resultSet.getLong("id"))
                            .userId(resultSet.getLong("user_id"))
                            .doctorId(resultSet.getLong("doctor_id"))
                            .appointmentDate(resultSet.getDate("appointment_date").toLocalDate())
                            .appointmentTime(resultSet.getTime("appointment_time").toLocalTime())
                            .status(resultSet.getString("status"))
                            .build());
                }
            }
        }
        return appointments;
    }
}