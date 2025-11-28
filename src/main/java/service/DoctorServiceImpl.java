// DoctorServiceImpl.java
package service;

import dao.DoctorDao;
import model.Doctor;
import java.sql.SQLException;
import java.util.List;

public class DoctorServiceImpl implements DoctorService {
    private final DoctorDao doctorDao;

    public DoctorServiceImpl(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDao.findAllDoctors();
    }

    @Override
    public Doctor getDoctorById(Long id) throws SQLException {
        return doctorDao.findDoctorById(id);
    }
}