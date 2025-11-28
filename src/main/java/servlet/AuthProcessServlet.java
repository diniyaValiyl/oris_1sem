package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;
import java.io.IOException;

@WebServlet("/auth/process")
public class AuthProcessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService userService = (UserService) getServletContext().getAttribute("userService");

        try {
            if ("login".equals(action)) {
                User user = userService.authenticateUser(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    response.sendRedirect("account");
                } else {
                    response.sendRedirect("auth?error=invalid");
                }
            } else if ("register".equals(action)) {
                // Простая регистрация
                userService.registerUser(
                        username,
                        password,
                        request.getParameter("fullName"),
                        request.getParameter("email"),
                        request.getParameter("phone"),
                        request.getParameter("gender"),
                        Integer.parseInt(request.getParameter("birthYear")),
                        request.getParameter("address")
                );
                response.sendRedirect("auth?success=registered");
            }
        } catch (Exception e) {
            response.sendRedirect("auth?error=" + e.getMessage());
        }
    }
}