package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter("/*")
public class AuthorisationFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Убираем contextPath из URI для проверки
        String path = requestURI.substring(contextPath.length());

        if (isPublicURI(path) || isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath + "/auth");
            return;
        }

        if (isAdminURI(path)) {
            User user = (User) session.getAttribute("user");
            String role = user.getRole();
            if (role == null || !role.equals("ADMIN")) {
                httpResponse.sendRedirect(contextPath + "/account");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicURI(String path) {
        return path.equals("/")
                || path.equals("/index")
                || path.equals("/auth")
                || path.equals("/register")
                || path.equals("/logout")
                || path.equals("/doctors")
                || path.equals("/doctor-detail")
                || path.equals("/main")
                || path.startsWith("/api/");
    }

    private boolean isAdminURI(String path) {
        return path.equals("/admin")
                || path.equals("/admin-dashboard")
                || path.startsWith("/admin/")
                || path.equals("/manage-doctors")
                || path.equals("/manage-appointments")
                || path.equals("/manage-users");
    }

    private boolean isStaticResource(String path) {
        return path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".gif")
                || path.endsWith(".ico")
                || path.endsWith(".jpeg")
                || path.endsWith(".svg")
                || path.endsWith(".woff")
                || path.endsWith(".woff2")
                || path.endsWith(".ttf")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/");
    }
}