<%-- schedule.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Расписание ${doctor.name} - Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/schedule.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="section">
            <div class="schedule-header">
                <h1>Расписание врача: ${doctor.name}</h1>
                <p class="specialization">${doctor.specialization}</p>

                <!-- Переключение недель -->
                <div class="week-selector">
                    <c:choose>
                        <c:when test="${isNextWeek}">
                            <a href="?week=current" class="btn-outline">← Текущая неделя</a>
                            <span class="current-week">Следующая неделя</span>
                            <span class="week-dates">${startOfWeek.plusDays(0)} - ${startOfWeek.plusDays(6)}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="current-week">Текущая неделя</span>
                            <span class="week-dates">${startOfWeek.plusDays(0)} - ${startOfWeek.plusDays(6)}</span>
                            <a href="?week=next" class="btn-outline">Следующая неделя →</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="schedule-table">
                <table>
                    <thead>
                        <tr>
                            <th>Время</th>
                            <th class="day-header">Понедельник<br>${startOfWeek.plusDays(0)}</th>
                            <th class="day-header">Вторник<br>${startOfWeek.plusDays(1)}</th>
                            <th class="day-header">Среда<br>${startOfWeek.plusDays(2)}</th>
                            <th class="day-header">Четверг<br>${startOfWeek.plusDays(3)}</th>
                            <th class="day-header">Пятница<br>${startOfWeek.plusDays(4)}</th>
                            <th class="day-header">Суббота<br>${startOfWeek.plusDays(5)}</th>
                            <th class="day-header">Воскресенье<br>${startOfWeek.plusDays(6)}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="timeSlot" items="${timeSlots}">
                            <tr>
                                <td class="time-column">${timeSlot}</td>
                                <c:forEach var="day" items="${weekDays}" varStatus="loop">
                                    <td>
                                        <c:set var="found" value="false" />
                                        <c:forEach var="slot" items="${weeklySchedule[day]}">
                                            <c:if test="${slot.time == timeSlot}">
                                                <c:set var="found" value="true" />
                                                <div class="time-slot ${slot.status}"
                                                     onclick="handleTimeSlotClick('${slot.date}', '${slot.time}', '${slot.status}')"
                                                     data-date="${slot.date}"
                                                     data-time="${slot.time}"
                                                     data-status="${slot.status}">
                                                    <c:choose>
                                                        <c:when test="${slot.status == 'available'}">
                                                            Свободно
                                                        </c:when>
                                                        <c:when test="${slot.status == 'booked'}">
                                                            Занято
                                                        </c:when>
                                                        <c:when test="${slot.status == 'past'}">
                                                            Прошедшее
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${not found}">
                                            <div class="time-slot unavailable">Не работает</div>
                                        </c:if>
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Форма записи -->
            <c:if test="${not empty sessionScope.user}">
                <div class="appointment-form" id="appointment-form" style="display: none;">
                    <h3>Запись на прием</h3>
                    <form action="${pageContext.request.contextPath}/appointment" method="post">
                        <input type="hidden" name="doctorId" value="${doctor.id}">
                        <input type="hidden" name="date" id="selected-date">
                        <input type="hidden" name="time" id="selected-time">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

                        <div class="form-group">
                            <label>Выбранное время:</label>
                            <span id="selected-time-display" class="selected-time"></span>
                        </div>

                        <div class="modal-actions">
                            <button type="submit" class="btn-primary">Подтвердить запись</button>
                            <button type="button" onclick="cancelSelection()" class="btn-outline">Отмена</button>
                        </div>
                    </form>
                </div>
            </c:if>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
    <script>
        function handleTimeSlotClick(date, time, status) {
            if (status !== 'available') {
                if (status === 'booked') {
                    alert('Это время уже занято. Пожалуйста, выберите другое время.');
                } else if (status === 'past') {
                    alert('Нельзя записаться на прошедшее время.');
                }
                return;
            }

            // Сбрасываем предыдущий выбор
            document.querySelectorAll('.time-slot.selected').forEach(s => s.classList.remove('selected'));

            // Выделяем выбранный слот
            const slot = document.querySelector(`.time-slot[data-date="\${date}"][data-time="\${time}"]`);
            slot.classList.add('selected');

            // Показываем форму записи
            showAppointmentForm(date, time);
        }

        function showAppointmentForm(date, time) {
            const form = document.getElementById('appointment-form');
            const dateDisplay = document.getElementById('selected-time-display');
            const dateInput = document.getElementById('selected-date');
            const timeInput = document.getElementById('selected-time');

            const formattedDate = new Date(date + 'T00:00:00').toLocaleDateString('ru-RU', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });

            dateDisplay.textContent = `${formattedDate} в ${time}`;
            dateInput.value = date;
            timeInput.value = time;

            form.style.display = 'block';
            form.scrollIntoView({ behavior: 'smooth' });
        }

        function cancelSelection() {
            document.querySelectorAll('.time-slot.selected').forEach(s => s.classList.remove('selected'));
            document.getElementById('appointment-form').style.display = 'none';
        }
    </script>
</body>
</html>