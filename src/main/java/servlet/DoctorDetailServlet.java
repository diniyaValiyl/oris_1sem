// DoctorDetailServlet.java
package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Doctor;
import service.DoctorService;
import java.io.IOException;

@WebServlet("/doctor/*")
public class DoctorDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect("/doctors");
            return;
        }

        String doctorIdStr = pathInfo.substring(1);
        try {
            Long doctorId = Long.parseLong(doctorIdStr);
            DoctorService doctorService = (DoctorService) getServletContext().getAttribute("doctorService");
            Doctor doctor = doctorService.getDoctorById(doctorId);

            if (doctor == null) {
                response.sendRedirect("/doctors");
                return;
            }

            request.setAttribute("doctor", doctor);
            request.getRequestDispatcher("/WEB-INF/views/doctor-detail.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("/doctors");
        }
    }
}