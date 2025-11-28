package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import util.HashUtil;
import dao.UserDao;
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

        // Получаем UserDao из контекста приложения
        UserDao userDao = (UserDao) getServletContext().getAttribute("userDao");

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

            // Валидация данных
            validateRegistrationData(username, password, confirmPassword, fullName, phone, birthYearStr, email, address, gender);

            // Проверка существования пользователя
            if (userDao.findUserByUsername(username) != null) {
                throw new Exception("Пользователь с таким логином уже существует");
            }

            // Создание пользователя
            User user = User.builder()
                    .username(username.trim())
                    .password(HashUtil.hashPassword(password))
                    .fullName(fullName.trim())
                    .email(email != null ? email.trim() : null)
                    .phone(phone.trim())
                    .gender(gender)
                    .birthYear(Integer.parseInt(birthYearStr))
                    .address(address.trim())
                    .role("PATIENT")
                    .build();

            // Сохранение в базу данных
            userDao.createUser(user);

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

    private void validateRegistrationData(String username, String password, String confirmPassword,
                                          String fullName, String phone, String birthYearStr,
                                          String email, String address, String gender) throws Exception {

        // Валидация логина
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Логин обязателен для заполнения");
        }

        username = username.trim();
        if (username.length() < 3) {
            throw new Exception("Логин должен содержать минимум 3 символа");
        }

        // Валидация пароля
        if (password == null || password.isEmpty()) {
            throw new Exception("Пароль обязателен для заполнения");
        }

        if (password.length() < 6) {
            throw new Exception("Пароль должен содержать минимум 6 символов");
        }

        // Проверка подтверждения пароля
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new Exception("Пароли не совпадают");
        }

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