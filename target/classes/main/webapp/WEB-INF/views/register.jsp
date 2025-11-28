<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section auth-section">
            <div class="auth-container">
                <h1>Регистрация</h1>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        ${error}
                    </div>
                </c:if>

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success">
                        <c:choose>
                            <c:when test="${param.success == 'registered'}">
                                Регистрация успешно завершена! Теперь вы можете
                                <a href="${pageContext.request.contextPath}/auth">войти</a> в систему.
                            </c:when>
                        </c:choose>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
                    <div class="form-group">
                        <label for="username">Логин:*</label>
                        <input type="text" id="username" name="username" required
                               placeholder="Минимум 3 символа" value="${username}">
                    </div>

                    <div class="form-group">
                        <label for="password">Пароль:*</label>
                        <input type="password" id="password" name="password" required
                               placeholder="Минимум 6 символов">
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Подтверждение пароля:*</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required
                               placeholder="Повторите пароль">
                    </div>

                    <div class="form-group">
                        <label for="fullName">ФИО:*</label>
                        <input type="text" id="fullName" name="fullName" required
                               placeholder="Ваше полное имя" value="${fullName}">
                    </div>

                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email"
                               placeholder="your@email.com" value="${email}">
                    </div>

                    <div class="form-group">
                        <label for="phone">Телефон:*</label>
                        <input type="tel" id="phone" name="phone" required
                               placeholder="+7 (XXX) XXX-XX-XX" value="${phone}">
                    </div>

                    <div class="form-group">
                        <label for="gender">Пол:*</label>
                        <select id="gender" name="gender" required>
                            <option value="">Выберите пол</option>
                            <option value="male" ${gender == 'male' ? 'selected' : ''}>Мужской</option>
                            <option value="female" ${gender == 'female' ? 'selected' : ''}>Женский</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="birthYear">Год рождения:*</label>
                        <input type="number" id="birthYear" name="birthYear"
                               min="1900" max="2025" required
                               placeholder="1990" value="${birthYear}">
                    </div>

                    <div class="form-group">
                        <label for="address">Адрес:*</label>
                        <textarea id="address" name="address" required
                                  placeholder="Ваш адрес проживания">${address}</textarea>
                    </div>

                    <button type="submit" class="btn-primary btn-full">Зарегистрироваться</button>
                </form>

                <div class="auth-links">
                    <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/auth">Войти</a></p>
                    <p><a href="${pageContext.request.contextPath}/index">← Вернуться на главную</a></p>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
    <script>
        // Валидация паролей на клиенте
        document.addEventListener('DOMContentLoaded', function() {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');

            function validatePasswords() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Пароли не совпадают');
                } else {
                    confirmPassword.setCustomValidity('');
                }
            }

            password.addEventListener('input', validatePasswords);
            confirmPassword.addEventListener('input', validatePasswords);
        });
    </script>
</body>
</html>