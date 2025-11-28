// AppointmentServiceImpl.java
package service;

import dao.AppointmentDao;
import model.Appointment;
import config.DatabaseConfig;
import java.sql.*;

        import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDao appointmentDao;

    public AppointmentServiceImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public void createAppointment(Long userId, Long doctorId, LocalDate date, LocalTime time) throws Exception {
        if (!appointmentDao.isTimeSlotAvailable(doctorId, date, time)) {
            throw new Exception("Выбранное время уже занято");
        }

        Appointment appointment = Appointment.builder()
                .userId(userId)
                .doctorId(doctorId)
                .appointmentDate(date)
                .appointmentTime(time)
                .status("SCHEDULED")
                .build();

        appointmentDao.createAppointment(appointment);
        incrementPatientCount();
    }

    @Override
    public List<Appointment> getUserAppointments(Long userId) throws SQLException {
        return appointmentDao.findAppointmentsByUserId(userId);
    }

    @Override
    public List<Appointment> getDoctorAppointmentsForDate(Long doctorId, LocalDate date) throws SQLException {
        return appointmentDao.findAppointmentsByDoctorAndDate(doctorId, date);
    }

    @Override
    public boolean isTimeSlotAvailable(Long doctorId, LocalDate date, LocalTime time) throws SQLException {
        return appointmentDao.isTimeSlotAvailable(doctorId, date, time);
    }

    @Override
    public List<Appointment> getDoctorAppointments(Long doctorId) throws SQLException {
        return appointmentDao.findAppointmentsByDoctorId(doctorId);
    }

    @Override
    public void incrementPatientCount() throws SQLException {
        String sql = "UPDATE clinic_settings SET setting_value = CAST(setting_value AS INTEGER) + 1 WHERE setting_key = 'patient_count'";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}