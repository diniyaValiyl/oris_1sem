package listener;

import model.User;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionListener implements HttpSessionListener {
    private final AtomicInteger activeSessions = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        // Установка таймаута сессии (30 минут)
        session.setMaxInactiveInterval(30 * 60);

        // Security settings
        session.setAttribute("createdTime", System.currentTimeMillis());

        int count = activeSessions.incrementAndGet();
        System.out.println("🆕 Сессия создана: " + session.getId() + ", активных сессий: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            System.out.println("🔚 Сессия завершена для пользователя: " + user.getUsername() +
                    ", ID сессии: " + session.getId());
        }

        int count = activeSessions.decrementAndGet();
        System.out.println("❌ Сессия уничтожена: " + session.getId() + ", активных сессий: " + count);
    }

    public int getActiveSessionsCount() {
        return activeSessions.get();
    }
}