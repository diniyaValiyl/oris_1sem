// AppointmentDao.java
package dao;

import model.Appointment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentDao {
    void createAppointment(Appointment appointment) throws SQLException;
    List<Appointment> findAppointmentsByUserId(Long userId) throws SQLException;
    List<Appointment> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) throws SQLException;
    boolean isTimeSlotAvailable(Long doctorId, LocalDate date, LocalTime time) throws SQLException;
    List<Appointment> findAppointmentsByDoctorId(Long doctorId) throws SQLException;
}

