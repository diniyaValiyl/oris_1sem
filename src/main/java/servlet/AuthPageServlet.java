package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");
        if ("register".equals(page)) {
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }
}