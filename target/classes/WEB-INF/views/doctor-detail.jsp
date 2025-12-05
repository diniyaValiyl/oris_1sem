<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${doctor.name} - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section">
            <!-- Простая навигация -->
            <div class="simple-nav">
                <a href="${pageContext.request.contextPath}/doctors" class="btn-outline">← Назад к врачам</a>
            </div>

            <div class="doctor-detail" style="display: flex; gap: 2rem; align-items: flex-start;">
                <div class="doctor-photo" style="flex-shrink: 0;">
                    <img src="${pageContext.request.contextPath}${doctor.photoPath}" alt="${doctor.name}"
                         style="width: 300px; height: 300px; border-radius: 10px; object-fit: cover;">
                </div>

                <div class="doctor-detail-info" style="flex: 1;">
                    <h1>${doctor.name}</h1>
                    <p class="specialization">${doctor.specialization}</p>
                    <p class="experience">Опыт работы: ${doctor.experience} лет</p>

                    <!-- Основные действия -->
                    <div class="doctor-actions" style="margin: 1.5rem 0;">
                        <a href="${pageContext.request.contextPath}/schedule/${doctor.id}" class="btn-primary">
                            Посмотреть расписание
                        </a>
                        <a href="${pageContext.request.contextPath}/doctors" class="btn-secondary">
                            Все врачи
                        </a>
                    </div>

                    <div class="doctor-description" style="margin: 1.5rem 0;">
                        <h3>О враче</h3>
                        <p>${doctor.description}</p>
                    </div>

                    <div class="doctor-education" style="margin: 1.5rem 0;">
                        <h3>Образование</h3>
                        <p>${doctor.education}</p>
                    </div>

                    <!-- Форма быстрой записи -->
                    <c:if test="${not empty sessionScope.user}">
                        <div class="appointment-form" style="background: var(--surface); padding: 1.5rem; border-radius: 10px; margin: 2rem 0;">
                            <h3>Запись на прием</h3>

                            <c:if test="${not empty param.error}">
                                <div class="alert alert-error">
                                    <c:choose>
                                        <c:when test="${param.error == 'invalid_date'}">Нельзя записаться на прошедшую дату</c:when>
                                        <c:when test="${param.error == 'invalid_date_format'}">Неверный формат даты</c:when>
                                        <c:otherwise>${param.error}</c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>

                            <c:if test="${not empty param.success}">
                                <div class="alert alert-success">
                                    Запись успешно создана!
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/appointment" method="post">
                                <input type="hidden" name="doctorId" value="${doctor.id}">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

                                <div class="form-group">
                                    <label for="date">Дата приема:*</label>
                                    <input type="date" id="date" name="date" required
                                           style="width: 100%; padding: 0.75rem; border: 1px solid var(--border); border-radius: 5px; background: var(--background); color: var(--text-primary);">
                                </div>

                                <div class="form-group">
                                    <label for="time">Время приема:*</label>
                                    <select id="time" name="time" required
                                            style="width: 100%; padding: 0.75rem; border: 1px solid var(--border); border-radius: 5px; background: var(--background); color: var(--text-primary);">
                                        <option value="">Выберите время</option>
                                        <option value="08:00">08:00</option>
                                        <option value="09:00">09:00</option>
                                        <option value="10:00">10:00</option>
                                        <option value="11:00">11:00</option>
                                        <option value="12:00">12:00</option>
                                        <option value="13:00">13:00</option>
                                        <option value="14:00">14:00</option>
                                        <option value="15:00">15:00</option>
                                        <option value="16:00">16:00</option>
                                        <option value="17:00">17:00</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn-primary" style="width: 100%;">Записаться на прием</button>
                            </form>
                        </div>
                    </c:if>

                    <c:if test="${empty sessionScope.user}">
                        <div class="auth-prompt">
                            <p>Для записи на прием необходимо <a href="${pageContext.request.contextPath}/auth">войти</a> в систему</p>
                            <p>Нет аккаунта? <a href="${pageContext.request.contextPath}/auth?page=register">Зарегистрируйтесь</a></p>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Простые ссылки для навигации -->
            <div class="simple-links">
                <a href="${pageContext.request.contextPath}/doctors">← Все врачи</a>
                <a href="${pageContext.request.contextPath}/schedule/${doctor.id}">Расписание врача →</a>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
    <script>
        // Установка минимальной даты (завтра)
        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('date');
            if (dateInput) {
                const tomorrow = new Date();
                tomorrow.setDate(tomorrow.getDate() + 1);
                const minDate = tomorrow.toISOString().split('T')[0];
                dateInput.min = minDate;

                if (!dateInput.value) {
                    dateInput.value = minDate;
                }
            }
        });
    </script>
</body>
</html>