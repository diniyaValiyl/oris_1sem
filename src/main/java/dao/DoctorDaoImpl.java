// DoctorDaoImpl.java
package dao;

import config.DatabaseConfig;
import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public List<Doctor> findAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY full_name";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                doctors.add(Doctor.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("full_name"))
                        .specialization(resultSet.getString("specialization"))
                        .description(resultSet.getString("description"))
                        .photoPath(resultSet.getString("photo_url"))
                        .education(resultSet.getString("education"))
                        .experience(resultSet.getInt("experience"))
                        .scheduleTemplate(resultSet.getString("schedule_template"))
                        .build());
            }
        }
        return doctors;
    }

    @Override
    public Doctor findDoctorById(Long id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Doctor.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("full_name"))
                            .specialization(resultSet.getString("specialization"))
                            .description(resultSet.getString("description"))
                            .photoPath(resultSet.getString("photo_url"))
                            .education(resultSet.getString("education"))
                            .experience(resultSet.getInt("experience"))
                            .scheduleTemplate(resultSet.getString("schedule_template"))
                            .build();
                }
            }
        }
        return null;
    }
}