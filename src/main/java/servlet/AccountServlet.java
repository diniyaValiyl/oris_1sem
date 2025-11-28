// AccountServlet.java (обновленный)
package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Appointment;
import model.User;
import service.AppointmentService;
import java.io.IOException;
import java.util.List;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("auth");
            return;
        }

        AppointmentService appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");

        try {
            List<Appointment> appointments = appointmentService.getUserAppointments(user.getId());
            request.setAttribute("appointments", appointments);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/account.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}