// Основная логика приложения
class DentalClinicApp {
    constructor() {
        this.init();
    }

    init() {
        this.setupSmoothScroll();
        this.setupModalHandlers();
        this.setupAppointmentHandlers();
        this.setupFormValidation();
    }

    // Плавная прокрутка к якорям
    setupSmoothScroll() {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }

    // Обработчики модальных окон
    setupModalHandlers() {
        // Модальное окно записи
        this.setupAppointmentModal();

        // Модальное окно графика работы
        this.setupScheduleModal();
    }

    setupAppointmentModal() {
        const modal = document.getElementById('appointment-modal');
        if (!modal) return;

        // Закрытие по клику вне окна
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                this.closeModal(modal);
            }
        });

        // Закрытие по ESC
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.style.display === 'flex') {
                this.closeModal(modal);
            }
        });
    }

    setupScheduleModal() {
        const scheduleBtn = document.getElementById('schedule-btn');
        const scheduleInfo = document.getElementById('schedule-info');

        if (scheduleBtn && scheduleInfo) {
            scheduleBtn.addEventListener('click', () => {
                scheduleInfo.style.display =
                    scheduleInfo.style.display === 'none' ? 'block' : 'none';
            });
        }
    }

    // Открытие модального окна записи
    openAppointmentModal(doctorId) {
        const modal = document.getElementById('appointment-modal');
        if (modal) {
            document.getElementById('appointment-doctor-id').value = doctorId;

            // Установка минимальной даты (завтра)
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            const minDate = tomorrow.toISOString().split('T')[0];

            const dateInput = document.getElementById('appointment-date');
            dateInput.min = minDate;
            dateInput.value = minDate;

            this.openModal(modal);
        }
    }

    // Закрытие модального окна
    closeAppointmentModal() {
        const modal = document.getElementById('appointment-modal');
        if (modal) {
            this.closeModal(modal);
        }
    }

    // Общие функции для модальных окон
    openModal(modal) {
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }

    closeModal(modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    // Обработчики для записи на прием
    setupAppointmentHandlers() {
        // Проверка доступности времени при выборе даты/времени
        const dateInput = document.getElementById('appointment-date');
        const timeInput = document.getElementById('appointment-time');

        if (dateInput && timeInput) {
            dateInput.addEventListener('change', this.validateAppointmentTime.bind(this));
            timeInput.addEventListener('change', this.validateAppointmentTime.bind(this));
        }
    }

    // Валидация времени записи
    validateAppointmentTime() {
        // Здесь можно добавить логику проверки доступности времени
        // Например, проверка через AJAX к серверу
        console.log('Validating appointment time...');
    }

    // Валидация форм
    setupFormValidation() {
        this.setupPhoneValidation();
        this.setupPasswordValidation();
    }

    setupPhoneValidation() {
        const phoneInputs = document.querySelectorAll('input[type="tel"]');
        phoneInputs.forEach(input => {
            input.addEventListener('input', (e) => {
                this.validatePhoneNumber(e.target);
            });
        });
    }

    validatePhoneNumber(input) {
        const phone = input.value.replace(/\D/g, '');
        if (phone.length < 10) {
            input.setCustomValidity('Номер телефона должен содержать не менее 10 цифр');
        } else {
            input.setCustomValidity('');
        }
    }

    setupPasswordValidation() {
        const passwordInputs = document.querySelectorAll('input[type="password"]');
        passwordInputs.forEach(input => {
            input.addEventListener('input', (e) => {
                this.validatePassword(e.target);
            });
        });
    }

    validatePassword(input) {
        const password = input.value;
        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasNumbers = /\d/.test(password);

        if (password.length < 6) {
            input.setCustomValidity('Пароль должен содержать минимум 6 символов');
        } else if (!hasUpperCase || !hasLowerCase || !hasNumbers) {
            input.setCustomValidity('Пароль должен содержать заглавные, строчные буквы и цифры');
        } else {
            input.setCustomValidity('');
        }
    }

    // Вспомогательные функции
    scrollToSection(sectionId) {
        const element = document.getElementById(sectionId);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    }

    showNotification(message, type = 'info') {
        // Реализация показа уведомлений
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        setTimeout(() => {
            notification.remove();
        }, 3000);
    }
}

// Глобальные функции для использования в HTML
function openAppointmentModal(doctorId) {
    if (window.dentalApp) {
        window.dentalApp.openAppointmentModal(doctorId);
    }
}

function closeAppointmentModal() {
    if (window.dentalApp) {
        window.dentalApp.closeAppointmentModal();
    }
}

function scrollToSection(sectionId) {
    if (window.dentalApp) {
        window.dentalApp.scrollToSection(sectionId);
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    window.dentalApp = new DentalClinicApp();
});