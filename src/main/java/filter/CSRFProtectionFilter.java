package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class CSRFProtectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession();

        // Генерация CSRF токена если его нет
        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }

        // Проверка CSRF для POST запросов (кроме аутентификации)
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

            // Исключаем аутентификацию из проверки CSRF
            if (path.equals("/auth/process")) {
                chain.doFilter(request, response);
                return;
            }

            String sessionToken = (String) session.getAttribute("csrfToken");
            String requestToken = httpRequest.getParameter("csrfToken");

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}