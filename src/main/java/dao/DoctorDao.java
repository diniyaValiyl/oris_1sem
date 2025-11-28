// DoctorDao.java
package dao;

import model.Doctor;
import java.sql.SQLException;
import java.util.List;

public interface DoctorDao {
    List<Doctor> findAllDoctors() throws SQLException;
    Doctor findDoctorById(Long id) throws SQLException;
}
