<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Врачи - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section">
            <h1>Наши врачи</h1>
            <div class="doctors-grid">
                <c:forEach var="doctor" items="${doctors}">
                    <div class="doctor-card-large">
                        <img src="${pageContext.request.contextPath}${doctor.photoPath}" alt="${doctor.name}">
                        <div class="doctor-info">
                            <h3>${doctor.name}</h3>
                            <p class="specialization">${doctor.specialization}</p>
                            <p class="experience">Опыт работы: ${doctor.experience} лет</p>
                            <p class="description">${doctor.description}</p>
                            <div class="doctor-actions">
                                <a href="${pageContext.request.contextPath}/doctor/${doctor.id}" class="btn-primary">Подробнее</a>
                                <a href="${pageContext.request.contextPath}/schedule/${doctor.id}" class="btn-secondary">Расписание</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
</body>
</html>