// countdown.js
class CountdownTimer {
    constructor() {
        this.timers = [];
        this.init();
    }

    init() {
        this.setupCountdowns();
        this.startTimers();
    }

    setupCountdowns() {
        const countdownElements = document.querySelectorAll('.countdown');

        countdownElements.forEach(element => {
            const date = element.dataset.date;
            const time = element.dataset.time;

            if (date && time) {
                this.timers.push({
                    element: element.querySelector('.countdown-timer'),
                    targetDate: new Date(`${date}T${time}`)
                });
            }
        });
    }

    startTimers() {
        setInterval(() => {
            this.updateAllTimers();
        }, 1000);
    }

    updateAllTimers() {
        const now = new Date();

        this.timers.forEach(timer => {
            this.updateTimer(timer, now);
        });
    }

    updateTimer(timer, now) {
        const diff = timer.targetDate - now;

        if (diff <= 0) {
            timer.element.textContent = 'Прием завершен';
            return;
        }

        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

        timer.element.textContent = `${days}д ${hours}ч ${minutes}м`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new CountdownTimer();
});