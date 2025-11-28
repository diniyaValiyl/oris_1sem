package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.AppointmentService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("auth");
            return;
        }

        try {
            Long doctorId = Long.parseLong(request.getParameter("doctorId"));
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            LocalTime time = LocalTime.parse(request.getParameter("time"));

            AppointmentService appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
            appointmentService.createAppointment(user.getId(), doctorId, date, time);

            response.sendRedirect("account?success=appointment_created");

        } catch (Exception e) {
            String doctorId = request.getParameter("doctorId");
            response.sendRedirect("doctor/" + doctorId + "?error=" + e.getMessage());
        }
    }
}