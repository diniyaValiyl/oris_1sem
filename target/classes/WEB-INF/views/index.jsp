<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Стоматологическая клиника "Вильдан"</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>Стоматологическая клиника "Вильдан"</h1>
                <p>Работаем с имплантацией, коронками уже более 15 лет. Современное оборудование, опытные специалисты, индивидуальный подход к каждому пациенту.</p>
                <a href="${pageContext.request.contextPath}/doctors" class="btn-primary">Записаться на прием</a>
            </div>
            <div class="hero-image">
                <img src="${pageContext.request.contextPath}/images/clinic-hero.jpg" alt="Клиника">
            </div>
        </section>

        <!-- About Section -->
        <section id="about" class="section">
            <h2>О клинике</h2>
            <div class="about-content">
                <p>Наша клиника предоставляет полный спектр стоматологических услуг с 2008 года. Мы гордимся своим опытом и современным подходом к лечению.</p>

                <div class="branches">
                    <div class="branch">
                        <div class="branch-image">
                            <img src="${pageContext.request.contextPath}/images/clinic1.jpg" alt="Филиал 1">
                        </div>
                        <div class="branch-info">
                            <h3>Основной филиал</h3>
                            <p>проспект Ямашева, 43. Оснащен современным оборудованием для диагностики и лечения.</p>
                        </div>
                    </div>
                    <div class="branch">
                        <div class="branch-info">
                            <h3>Детское отделение</h3>
                            <p>ул. Чистопольская, 15. Специализированное отделение для маленьких пациентов.</p>
                        </div>
                        <div class="branch-image">
                            <img src="${pageContext.request.contextPath}/images/clinic2.jpg" alt="Филиал 2">
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Doctors Preview -->
        <section class="section">
            <h2>Наши врачи</h2>
            <div class="doctors-preview">
                <c:forEach var="doctor" items="${doctors}" end="2">
                    <div class="doctor-card">
                        <img src="${pageContext.request.contextPath}${doctor.photoPath}" alt="${doctor.name}">
                        <h3>${doctor.name}</h3>
                        <p>${doctor.specialization}</p>
                        <a href="${pageContext.request.contextPath}/doctor/${doctor.id}" class="btn-outline">Подробнее</a>
                    </div>
                </c:forEach>
            </div>
            <div style="text-align: center; margin-top: 2rem;">
                <a href="${pageContext.request.contextPath}/doctors" class="btn-primary">Все врачи</a>
            </div>
        </section>

        <!-- Achievements Section -->
        <section id="achievements" class="section">
            <h2>Достижения</h2>
            <div class="achievements-content">
                <div class="certificates-grid">
                    <img src="${pageContext.request.contextPath}/images/certificate1.jpg" alt="Сертификат 1">
                    <img src="${pageContext.request.contextPath}/images/certificate2.jpg" alt="Сертификат 2">
                    <img src="${pageContext.request.contextPath}/images/certificate3.jpg" alt="Сертификат 3">
                </div>

                <div class="stats">
                    <div class="stat-item">
                        <h3>15,000+</h3>
                        <p>Довольных пациентов</p>
                    </div>
                </div>

                <!-- Reviews Section -->
                <div class="reviews-section">
                    <h3>Отзывы пациентов</h3>
                    <div class="reviews-container">
                        <c:forEach var="review" items="${reviews}">
                            <div class="review-card">
                                <div class="review-header">
                                    <strong>${review.userName}</strong>
                                    <div class="rating">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= review.rating}">★</c:when>
                                                <c:otherwise>☆</c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </div>
                                <p>${review.reviewText}</p>
                            </div>
                        </c:forEach>
                    </div>

                    <c:if test="${not empty sessionScope.user}">
                        <div class="review-form">
                            <h4>Оставить отзыв</h4>
                            <form action="${pageContext.request.contextPath}/review" method="post">
                                <textarea name="reviewText" placeholder="Ваш отзыв" required></textarea>
                                <select name="rating" required>
                                    <option value="">Оценка</option>
                                    <option value="5">★★★★★</option>
                                    <option value="4">★★★★</option>
                                    <option value="3">★★★</option>
                                    <option value="2">★★</option>
                                    <option value="1">★</option>
                                </select>
                                <button type="submit" class="btn-primary">Отправить</button>
                            </form>
                        </div>
                    </c:if>
                </div>

                <div class="insurance">
                    <h3>Сотрудничаем со страховыми компаниями</h3>
                    <p>Принимаем пациентов по полисам ДМС ведущих страховых компаний.</p>
                </div>
            </div>
        </section>

        <!-- Contacts Section -->
        <section id="contacts" class="section">
            <h2>Контакты</h2>
            <div class="contacts-content">
                <div class="contact-info">
                    <p><strong>Адрес:</strong> проспект Ямашева, 43</p>
                    <p><strong>Телефон:</strong> <a href="tel:+79179091717">+7 (917) 909-17-17</a></p>
                    <div class="schedule">
                        <h4>График работы:</h4>
                        <p>Пн-Пт: 08:00 - 20:00</p>
                        <p>Сб: 08:00 - 14:00</p>
                        <p>Вс: Выходной</p>
                    </div>
                </div>

                <div class="feedback-form">
                    <h3>Обратная связь</h3>
                    <form id="feedback-form">
                        <input type="tel" id="feedback-phone" placeholder="Номер телефона" required>
                        <textarea id="feedback-situation" placeholder="Ваша ситуация" required></textarea>
                        <button type="submit" class="btn-primary">Отправить</button>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/theme.js"></script>
    <script src="${pageContext.request.contextPath}/js/feedback.js"></script>
</body>
</html>