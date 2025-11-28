// AppointmentService.java
package service;

import model.Appointment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    void createAppointment(Long userId, Long doctorId, LocalDate date, LocalTime time) throws Exception;
    List<Appointment> getUserAppointments(Long userId) throws SQLException;
    List<Appointment> getDoctorAppointmentsForDate(Long doctorId, LocalDate date) throws SQLException;
    boolean isTimeSlotAvailable(Long doctorId, LocalDate date, LocalTime time) throws SQLException;
    List<Appointment> getDoctorAppointments(Long doctorId) throws SQLException;
    void incrementPatientCount() throws SQLException;
}
