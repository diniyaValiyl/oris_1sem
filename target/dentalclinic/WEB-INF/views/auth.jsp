<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section">
            <div style="max-width: 400px; margin: 0 auto; padding: 2rem;">
                <h1 style="text-align: center; margin-bottom: 2rem;">Вход в личный кабинет</h1>

                <%-- Показываем ошибки --%>
                <c:if test="${not empty param.error}">
                    <div style="background: #ffebee; color: #c62828; padding: 1rem; border-radius: 5px; margin-bottom: 1rem; border: 1px solid #ffcdd2;">
                        <c:choose>
                            <c:when test="${param.error == 'invalid'}">Неверный логин или пароль</c:when>
                            <c:when test="${param.error == 'system'}">Ошибка системы. Попробуйте позже.</c:when>
                            <c:otherwise>Ошибка авторизации</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <%-- Форма входа с абсолютным путем --%>
                <form action="${pageContext.request.contextPath}/auth/process" method="post" style="display: flex; flex-direction: column; gap: 1rem;">
                    <div>
                        <label for="username" style="display: block; margin-bottom: 0.5rem; font-weight: bold;">Логин:</label>
                        <input type="text" id="username" name="username" required
                               style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 5px;"
                               placeholder="Введите ваш логин" value="testuser">
                    </div>

                    <div>
                        <label for="password" style="display: block; margin-bottom: 0.5rem; font-weight: bold;">Пароль:</label>
                        <input type="password" id="password" name="password" required
                               style="width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 5px;"
                               placeholder="Введите ваш пароль" value="test123">
                    </div>

                    <button type="submit"
                            style="background: #2e7d32; color: white; padding: 0.75rem; border: none; border-radius: 5px; cursor: pointer; font-size: 1rem;">
                        Войти
                    </button>
                </form>

                <div style="text-align: center; margin-top: 1.5rem;">
                    <p>Нет аккаунта?
                        <a href="${pageContext.request.contextPath}/register" style="color: #2e7d32;">Зарегистрироваться</a>
                    </p>
                    <p style="margin-top: 0.5rem;">
                        <a href="${pageContext.request.contextPath}/index" style="color: #666;">← Вернуться на главную</a>
                    </p>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>
</body>
</html>