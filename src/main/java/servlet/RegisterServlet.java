package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.UserService;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Показываем страницу регистрации
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService userService = (UserService) getServletContext().getAttribute("userService");

        try {
            // Получаем параметры формы
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String birthYearStr = request.getParameter("birthYear");
            String address = request.getParameter("address");

            // Валидация основных данных
            userService.validateRegistrationData(username, password, confirmPassword);

            // Дополнительная валидация
            validateAdditionalData(fullName, phone, birthYearStr, address, gender);

            // Создание пользователя
            User user = User.builder()
                    .username(username.trim())
                    .password(password) // Пароль будет захеширован в сервисе
                    .fullName(fullName.trim())
                    .email(email != null ? email.trim() : null)
                    .phone(phone.trim())
                    .gender(gender)
                    .birthYear(Integer.parseInt(birthYearStr))
                    .address(address.trim())
                    .role("PATIENT")
                    .build();

            // Регистрация пользователя (теперь без приведения типов)
            userService.registerUser(user);

            // Перенаправление на страницу входа с сообщением об успехе
            response.sendRedirect(request.getContextPath() + "/auth?success=registered");

        } catch (Exception e) {
            // В случае ошибки возвращаем на страницу регистрации с сообщением об ошибке
            request.setAttribute("error", e.getMessage());
            // Сохраняем введенные данные для повторного заполнения формы
            saveFormData(request);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private void validateAdditionalData(String fullName, String phone, String birthYearStr,
                                        String address, String gender) throws Exception {
        // Валидация ФИО
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new Exception("ФИО обязательно для заполнения");
        }

        // Валидация телефона
        if (phone == null || phone.trim().isEmpty()) {
            throw new Exception("Телефон обязателен для заполнения");
        }

        // Валидация года рождения
        if (birthYearStr == null || birthYearStr.trim().isEmpty()) {
            throw new Exception("Год рождения обязателен");
        }

        try {
            int birthYear = Integer.parseInt(birthYearStr.trim());
            int currentYear = java.time.Year.now().getValue();
            if (birthYear < 1900 || birthYear > currentYear) {
                throw new Exception("Неверный год рождения");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Год рождения должен быть числом");
        }

        // Валидация адреса
        if (address == null || address.trim().isEmpty()) {
            throw new Exception("Адрес обязателен для заполнения");
        }

        // Валидация пола
        if (gender == null || gender.trim().isEmpty()) {
            throw new Exception("Пол обязателен для выбора");
        }
    }

    private void saveFormData(HttpServletRequest request) {
        // Сохраняем введенные данные в атрибуты запроса для повторного заполнения формы
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("fullName", request.getParameter("fullName"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("phone", request.getParameter("phone"));
        request.setAttribute("gender", request.getParameter("gender"));
        request.setAttribute("birthYear", request.getParameter("birthYear"));
        request.setAttribute("address", request.getParameter("address"));
    }
}