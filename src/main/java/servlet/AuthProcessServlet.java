package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.UserService;
import java.io.IOException;

@WebServlet("/auth/process")
public class AuthProcessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService userService = (UserService) getServletContext().getAttribute("userService");

        try {
            User user = userService.authenticateUser(username, password);
            if (user != null) {
                // Создаем сессию
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Устанавливаем таймаут сессии (30 минут)
                session.setMaxInactiveInterval(30 * 60);

                // Логируем успешный вход
                System.out.println("Пользователь " + username + " успешно авторизован");

                // Редирект на страницу аккаунта
                response.sendRedirect(request.getContextPath() + "/account");
            } else {
                System.out.println("Неверные учетные данные для пользователя: " + username);
                response.sendRedirect(request.getContextPath() + "/auth?error=invalid");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при аутентификации: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/auth?error=system_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Перенаправляем GET запросы на страницу аутентификации
        response.sendRedirect(request.getContextPath() + "/auth");
    }
}