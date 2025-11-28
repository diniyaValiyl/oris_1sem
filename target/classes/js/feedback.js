class FeedbackForm {
    constructor() {
        this.storageKey = 'clinic_feedbacks';
        this.init();
    }

    init() {
        this.setupFeedbackForm();
        this.setupPhoneValidation();
        this.loadSavedFeedbacks();
    }

    setupFeedbackForm() {
        const form = document.getElementById('feedback-form');
        if (form) {
            form.addEventListener('submit', (e) => this.handleSubmit(e));
        }
    }

    setupPhoneValidation() {
        const phoneInput = document.getElementById('feedback-phone');
        if (phoneInput) {
            phoneInput.addEventListener('input', (e) => {
                this.formatPhoneNumber(e.target);
                this.validatePhoneNumber(e.target);
            });

            phoneInput.addEventListener('blur', (e) => {
                this.validatePhoneNumber(e.target);
            });
        }
    }

    formatPhoneNumber(input) {
        let value = input.value.replace(/\D/g, '');

        if (value.length > 0) {
            if (value.length <= 3) {
                value = value;
            } else if (value.length <= 6) {
                value = `+7 (${value.slice(1, 4)}) ${value.slice(4)}`;
            } else if (value.length <= 8) {
                value = `+7 (${value.slice(1, 4)}) ${value.slice(4, 7)}-${value.slice(7)}`;
            } else {
                value = `+7 (${value.slice(1, 4)}) ${value.slice(4, 7)}-${value.slice(7, 9)}-${value.slice(9, 11)}`;
            }
        }

        input.value = value;
    }

    validatePhoneNumber(input) {
        const phone = input.value.replace(/\D/g, '');

        if (phone.length === 0) {
            input.setCustomValidity('Поле телефона обязательно для заполнения');
        } else if (phone.length < 11) {
            input.setCustomValidity('Номер телефона должен содержать 11 цифр');
        } else {
            input.setCustomValidity('');
        }

        this.updateValidationStyle(input);
    }

    updateValidationStyle(input) {
        if (input.validity.valid) {
            input.style.borderColor = '#4caf50';
        } else {
            input.style.borderColor = '#f44336';
        }
    }

    handleSubmit(e) {
        e.preventDefault();

        const formData = this.getFormData();

        if (this.validateForm(formData)) {
            this.saveFeedback(formData);
            this.showSuccessMessage();
            this.resetForm();
            this.sendToServer(formData); // Имитация отправки на сервер
        }
    }

    getFormData() {
        return {
            phone: document.getElementById('feedback-phone').value,
            situation: document.getElementById('feedback-situation').value,
            timestamp: new Date().toISOString(),
            id: Date.now().toString()
        };
    }

    validateForm(data) {
        if (!data.phone || data.phone.replace(/\D/g, '').length < 11) {
            this.showError('Пожалуйста, введите корректный номер телефона');
            return false;
        }

        if (!data.situation || data.situation.trim().length < 10) {
            this.showError('Пожалуйста, опишите вашу ситуацию более подробно (минимум 10 символов)');
            return false;
        }

        if (data.situation.length > 1000) {
            this.showError('Описание ситуации не должно превышать 1000 символов');
            return false;
        }

        return true;
    }

    saveFeedback(data) {
        let feedbacks = this.getSavedFeedbacks();
        feedbacks.push(data);

        try {
            localStorage.setItem(this.storageKey, JSON.stringify(feedbacks));
            console.log('Feedback saved locally:', data);
        } catch (error) {
            console.error('Error saving feedback to localStorage:', error);
        }
    }

    getSavedFeedbacks() {
        try {
            return JSON.parse(localStorage.getItem(this.storageKey)) || [];
        } catch (error) {
            console.error('Error loading feedbacks from localStorage:', error);
            return [];
        }
    }

    loadSavedFeedbacks() {
        const feedbacks = this.getSavedFeedbacks();
        console.log('Loaded feedbacks:', feedbacks.length);
        return feedbacks;
    }

    sendToServer(data) {
        // Имитация отправки на сервер
        setTimeout(() => {
            console.log('Feedback sent to server:', data);

            // В реальном приложении здесь был бы AJAX запрос
            // fetch('/api/feedback', {
            //     method: 'POST',
            //     headers: { 'Content-Type': 'application/json' },
            //     body: JSON.stringify(data)
            // });
        }, 1000);
    }

    showSuccessMessage() {
        this.showMessage('Спасибо! Ваше сообщение отправлено. Мы свяжемся с вами в ближайшее время.', 'success');
    }

    showError(message) {
        this.showMessage(message, 'error');
    }

    showMessage(message, type = 'info') {
        // Создание элемента уведомления
        const notification = document.createElement('div');
        notification.className = `feedback-notification feedback-${type}`;
        notification.innerHTML = `
            <div class="feedback-notification-content">
                <span class="feedback-notification-message">${message}</span>
                <button class="feedback-notification-close">&times;</button>
            </div>
        `;

        // Стили для уведомления
        notification.style.cssText = `
            position: fixed;
            top: 100px;
            right: 20px;
            background: ${type === 'error' ? '#f44336' : type === 'success' ? '#4caf50' : '#2196f3'};
            color: white;
            padding: 1rem;
            border-radius: 5px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 10000;
            max-width: 400px;
            animation: slideIn 0.3s ease;
        `;

        document.body.appendChild(notification);

        // Кнопка закрытия
        const closeBtn = notification.querySelector('.feedback-notification-close');
        closeBtn.addEventListener('click', () => {
            notification.remove();
        });

        // Автоматическое закрытие
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 5000);
    }

    resetForm() {
        const form = document.getElementById('feedback-form');
        if (form) {
            form.reset();

            // Сброс стилей валидации
            const inputs = form.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                input.style.borderColor = '';
            });
        }
    }

    // Статистика по фидбекам
    getFeedbackStats() {
        const feedbacks = this.getSavedFeedbacks();
        return {
            total: feedbacks.length,
            lastWeek: feedbacks.filter(fb => {
                const weekAgo = new Date();
                weekAgo.setDate(weekAgo.getDate() - 7);
                return new Date(fb.timestamp) > weekAgo;
            }).length,
            lastMonth: feedbacks.filter(fb => {
                const monthAgo = new Date();
                monthAgo.setMonth(monthAgo.getMonth() - 1);
                return new Date(fb.timestamp) > monthAgo;
            }).length
        };
    }
}

// CSS для анимаций
const feedbackStyles = `
@keyframes slideIn {
    from {
        transform: translateX(100%);
        opacity: 0;
    }
    to {
        transform: translateX(0);
        opacity: 1;
    }
}

.feedback-notification-close {
    background: none;
    border: none;
    color: white;
    font-size: 1.2rem;
    cursor: pointer;
    margin-left: 1rem;
}

.feedback-notification-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
`;

// Добавление стилей
const feedbackStyleSheet = document.createElement('style');
feedbackStyleSheet.textContent = feedbackStyles;
document.head.appendChild(feedbackStyleSheet);

// Инициализация
document.addEventListener('DOMContentLoaded', () => {
    window.feedbackForm = new FeedbackForm();
});