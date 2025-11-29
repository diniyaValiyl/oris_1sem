<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section auth-section">
            <div class="auth-container">
                <h1>Вход в личный кабинет</h1>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-error">
                        <c:choose>
                            <c:when test="${param.error == 'invalid'}">Неверный логин или пароль</c:when>
                            <c:when test="${param.error == 'system_error'}">Системная ошибка. Попробуйте позже.</c:when>
                            <c:otherwise>Ошибка авторизации: ${param.error}</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success">
                        <c:choose>
                            <c:when test="${param.success == 'registered'}">Регистрация успешно завершена! Теперь вы можете войти.</c:when>
                        </c:choose>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/auth/process" method="post" class="auth-form">
                    <div class="form-group">
                        <label for="username">Логин:</label>
                        <input type="text" id="username" name="username" required
                               placeholder="Введите ваш логин" value="${param.username}">
                    </div>

                    <div class="form-group">
                        <label for="password">Пароль:</label>
                        <input type="password" id="password" name="password" required
                               placeholder="Введите ваш пароль">
                    </div>

                    <button type="submit" class="btn-primary btn-full">Войти</button>
                </form>

                <div class="auth-links">
                    <p>Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Зарегистрироваться</a></p>
                    <p><a href="${pageContext.request.contextPath}/index">← Вернуться на главную</a></p>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</body>
</html>