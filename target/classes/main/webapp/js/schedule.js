// schedule.js
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
    const slot = document.querySelector(`.time-slot[data-date="${date}"][data-time="${time}"]`);
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

    dateDisplay.textContent = `${formattedDate}`;
    dateInput.value = date;
    timeInput.value = time;

    form.style.display = 'block';
    form.scrollIntoView({ behavior: 'smooth' });
}

function cancelSelection() {
    document.querySelectorAll('.time-slot.selected').forEach(s => s.classList.remove('selected'));
    document.getElementById('appointment-form').style.display = 'none';
}