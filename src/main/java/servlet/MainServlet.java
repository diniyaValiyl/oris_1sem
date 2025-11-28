package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Review;
import service.ReviewService;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReviewService reviewService = (ReviewService) getServletContext().getAttribute("reviewService");

        try {
            List<Review> reviews = reviewService.getAllReviews();
            request.setAttribute("reviews", reviews);

            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}