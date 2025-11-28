package util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Pattern;

public class SecurityUtil {
    private static final String CSRF_TOKEN_NAME = "csrfToken";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s-]{2,50}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[\\d\\s\\-\\(\\)\\+]{10,20}$");

    // CSRF Protection
    public static String generateCSRFToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean validateCSRFToken(HttpServletRequest request) {
        String sessionToken = (String) request.getSession().getAttribute(CSRF_TOKEN_NAME);
        String requestToken = request.getParameter(CSRF_TOKEN_NAME);

        return sessionToken != null && sessionToken.equals(requestToken);
    }

    public static void storeCSRFToken(HttpServletRequest request) {
        String token = generateCSRFToken();
        request.getSession().setAttribute(CSRF_TOKEN_NAME, token);
        request.setAttribute(CSRF_TOKEN_NAME, token);
    }

    // Input Validation
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String digits = phone.replaceAll("\\D", "");
        return digits.length() >= 10 && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 100;
    }

    public static boolean isValidUsername(String username) {
        return username != null &&
                username.length() >= 3 &&
                username.length() <= 50 &&
                username.matches("^[a-zA-Z0-9_]+$");
    }

    // Input Sanitization
    public static String sanitizeInput(String input) {
        if (input == null) return null;

        return input.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("/", "&#x2F;")
                .replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;")
                .replaceAll("&", "&amp;");
    }

    public static String sanitizeHTML(String input) {
        if (input == null) return null;

        // Разрешаем только безопасные HTML теги
        return input.replaceAll("<script", "&lt;script")
                .replaceAll("</script", "&lt;/script")
                .replaceAll("<iframe", "&lt;iframe")
                .replaceAll("</iframe", "&lt;/iframe")
                .replaceAll("<object", "&lt;object")
                .replaceAll("</object", "&lt;/object")
                .replaceAll("<embed", "&lt;embed")
                .replaceAll("</embed", "&lt;/embed")
                .replaceAll("<link", "&lt;link");
    }

    // SQL Injection Protection
    public static String escapeSQL(String input) {
        if (input == null) return null;

        return input.replace("'", "''")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\0", "\\0")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\u001A", "\\Z")
                .replace("\u0000", "\\0");
    }
}