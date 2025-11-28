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
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("account");
            } else {
                response.sendRedirect("auth?error=invalid");
            }
        } catch (Exception e) {
            response.sendRedirect("auth?error=" + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("auth");
    }
}