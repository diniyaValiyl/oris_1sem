<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section">
            <h1>Личный кабинет</h1>

            <div class="account-content">
                <!-- Информация о пользователе -->
                <div class="user-info">
                    <h2>Мои данные</h2>
                    <div class="info-grid">
                        <div class="info-item">
                            <label>ФИО:</label>
                            <span>${user.fullName}</span>
                        </div>
                        <div class="info-item">
                            <label>Email:</label>
                            <span>${user.email}</span>
                        </div>
                        <div class="info-item">
                            <label>Телефон:</label>
                            <span>${user.phone}</span>
                        </div>
                        <div class="info-item">
                            <label>Пол:</label>
                            <span>
                                <c:choose>
                                    <c:when test="${user.gender == 'male'}">Мужской</c:when>
                                    <c:when test="${user.gender == 'female'}">Женский</c:when>
                                    <c:otherwise>Не указан</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="info-item">
                            <label>Год рождения:</label>
                            <span>${user.birthYear}</span>
                        </div>
                        <div class="info-item">
                            <label>Адрес:</label>
                            <span>${user.address}</span>
                        </div>
                    </div>
                </div>

                <!-- Мои записи -->
                <div class="appointments-section">
                    <h2>Мои записи</h2>
                    <c:choose>
                        <c:when test="${not empty appointments}">
                            <div class="appointments-list">
                                <c:forEach var="appointment" items="${appointments}">
                                    <div class="appointment-card">
                                        <div class="appointment-info">
                                            <h4>${appointment.doctorName}</h4>
                                            <p>Дата: ${appointment.appointmentDate}</p>
                                            <p>Время: ${appointment.appointmentTime}</p>
                                            <span class="status ${appointment.status.toLowerCase()}">${appointment.status}</span>
                                        </div>
                                        <div class="countdown" data-date="${appointment.appointmentDate}" data-time="${appointment.appointmentTime}">
                                            До приема осталось: <span class="countdown-timer"></span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p>У вас нет активных записей</p>
                            <a href="${pageContext.request.contextPath}/doctors" class="btn-primary">Записаться на прием</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
    <script src="${pageContext.request.contextPath}/js/countdown.js"></script>
</body>
</html>