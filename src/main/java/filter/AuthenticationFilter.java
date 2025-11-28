package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Публичные ресурсы
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") ||
                path.equals("/") || path.equals("/index") || path.equals("/auth") ||
                path.equals("/auth/process") || path.equals("/doctors") || path.startsWith("/doctor/")) {
            chain.doFilter(request, response);
            return;
        }

        // Защищенные ресурсы требуют авторизации
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth");
            return;
        }

        chain.doFilter(request, response);
    }
}