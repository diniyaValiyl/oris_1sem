// DoctorsServlet.java
package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Doctor;
import service.DoctorService;
import java.io.IOException;
import java.util.List;

@WebServlet("/doctors")
public class DoctorsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DoctorService doctorService = (DoctorService) getServletContext().getAttribute("doctorService");

        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            request.setAttribute("doctors", doctors);
            request.getRequestDispatcher("/WEB-INF/views/doctors.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}