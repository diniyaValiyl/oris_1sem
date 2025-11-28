<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <nav class="navbar">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/index">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Логотип клиники">
                <span>Клиника "Вильдан"</span>
            </a>
        </div>

        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/index" class="nav-link">Главная</a></li>
            <li><a href="${pageContext.request.contextPath}/doctors" class="nav-link">Врачи</a></li>
            <li><a href="${pageContext.request.contextPath}/index#achievements" class="nav-link">Достижения</a></li>
            <li><a href="${pageContext.request.contextPath}/index#contacts" class="nav-link">Контакты</a></li>

            <c:if test="${not empty sessionScope.user}">
                <li><a href="${pageContext.request.contextPath}/account" class="nav-link">Мои записи</a></li>
            </c:if>
        </ul>

        <div class="nav-right">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <div class="user-info">
                        <span class="user-name">${user.fullName}</span>
                        <div class="user-actions">
                            <a href="${pageContext.request.contextPath}/account" class="btn-secondary">Личный кабинет</a>
                            <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                                <button type="submit" class="btn-outline">Выйти</button>
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="auth-buttons">
                        <a href="${pageContext.request.contextPath}/auth" class="btn-secondary">Войти</a>
                        <a href="${pageContext.request.contextPath}/auth?page=register" class="btn-primary">Регистрация</a>
                    </div>
                </c:otherwise>
            </c:choose>
            <button id="theme-toggle" class="theme-btn">Ночь</button>
        </div>
    </nav>
</header>