// DoctorService.java
package service;

import model.Doctor;
import java.sql.SQLException;
import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors() throws SQLException;
    Doctor getDoctorById(Long id) throws SQLException;
}