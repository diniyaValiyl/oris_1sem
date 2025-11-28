package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.AppointmentService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("auth");
            return;
        }

        // Простая защита от повторной отправки
        Long lastAppointmentTime = (Long) session.getAttribute("lastAppointmentTime");
        long currentTime = System.currentTimeMillis();

        if (lastAppointmentTime != null && (currentTime - lastAppointmentTime) < 3000) {
            response.sendRedirect("account?error=too_fast");
            return;
        }

        session.setAttribute("lastAppointmentTime", currentTime);

        try {
            // Получаем параметры из формы
            Long doctorId = Long.parseLong(request.getParameter("doctorId"));
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            LocalTime time = LocalTime.parse(request.getParameter("time"));

            // Простая валидация даты
            if (date.isBefore(LocalDate.now())) {
                response.sendRedirect("doctor/" + doctorId + "?error=invalid_date");
                return;
            }

            // Проверяем, что время в будущем (если дата сегодня)
            if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
                response.sendRedirect("doctor/" + doctorId + "?error=invalid_time");
                return;
            }

            AppointmentService appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");

            // Создаем запись
            appointmentService.createAppointment(user.getId(), doctorId, date, time);

            // Перенаправляем с сообщением об успехе
            response.sendRedirect("account?success=appointment_created");

        } catch (NumberFormatException e) {
            // Ошибка парсинга doctorId
            response.sendRedirect("doctors?error=invalid_doctor");
        } catch (java.time.format.DateTimeParseException e) {
            // Ошибка парсинга даты/времени
            String doctorId = request.getParameter("doctorId");
            if (doctorId != null) {
                response.sendRedirect("doctor/" + doctorId + "?error=invalid_date_format");
            } else {
                response.sendRedirect("doctors?error=invalid_date_format");
            }
        } catch (Exception e) {
            // Другие ошибки (время занято и т.д.)
            String doctorId = request.getParameter("doctorId");
            if (doctorId != null) {
                response.sendRedirect("doctor/" + doctorId + "?error=" + e.getMessage());
            } else {
                response.sendRedirect("doctors?error=unknown_error");
            }
        }
    }
}