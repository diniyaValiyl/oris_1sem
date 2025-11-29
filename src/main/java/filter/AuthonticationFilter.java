package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter("/*")
public class AuthonticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Убираем contextPath из URI для проверки
        String path = requestURI.substring(contextPath.length());

        // Публичные URL, которые не требуют аутентификации
        if (isPublicResource(path) || isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Проверяем аутентификацию для защищенных URL
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            // Пользователь не аутентифицирован - перенаправляем на страницу входа
            httpResponse.sendRedirect(contextPath + "/auth");
            return;
        }

        // Пользователь аутентифицирован - продолжаем цепочку фильтров
        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String path) {
        return path.equals("/") ||
                path.equals("/index") ||
                path.equals("/auth") ||
                path.equals("/auth/process") ||
                path.equals("/register") ||
                path.equals("/doctors") ||
                path.equals("/doctor-detail") ||
                path.startsWith("/api/") ||
                path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/images/");
    }

    private boolean isStaticResource(String path) {
        return path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".jpeg") ||
                path.endsWith(".gif") ||
                path.endsWith(".ico") ||
                path.endsWith(".svg");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация фильтра
    }

    @Override
    public void destroy() {
        // Очистка ресурсов фильтра
    }
}