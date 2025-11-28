// ReviewServlet.java
package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import service.ReviewService;
import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("auth");
            return;
        }

        String reviewText = request.getParameter("reviewText");
        Integer rating = Integer.parseInt(request.getParameter("rating"));
        ReviewService reviewService = (ReviewService) getServletContext().getAttribute("reviewService");

        try {
            reviewService.createReview(user.getId(), reviewText, rating);
            response.sendRedirect("index?success=review_added");
        } catch (Exception e) {
            response.sendRedirect("index?error=" + e.getMessage());
        }
    }
}