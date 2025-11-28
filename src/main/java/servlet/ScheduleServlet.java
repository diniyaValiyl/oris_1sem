package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Doctor;
import model.Appointment;
import service.DoctorService;
import service.AppointmentService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/schedule/*")
public class ScheduleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/doctors");
            return;
        }

        String doctorIdStr = pathInfo.substring(1);
        try {
            Long doctorId = Long.parseLong(doctorIdStr);
            DoctorService doctorService = (DoctorService) getServletContext().getAttribute("doctorService");
            Doctor doctor = doctorService.getDoctorById(doctorId);

            if (doctor == null) {
                response.sendRedirect(request.getContextPath() + "/doctors");
                return;
            }

            // Простой выбор недели: текущая или следующая
            String weekParam = request.getParameter("week");
            boolean isNextWeek = "next".equals(weekParam);

            // Генерация расписания
            Map<String, List<TimeSlot>> weeklySchedule = generateWeeklySchedule(doctorId, isNextWeek);

            // Даты для отображения
            LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
            if (isNextWeek) {
                startOfWeek = startOfWeek.plusWeeks(1);
            }

            request.setAttribute("doctor", doctor);
            request.setAttribute("weeklySchedule", weeklySchedule);
            request.setAttribute("timeSlots", generateTimeSlots());
            request.setAttribute("weekDays", getWeekDays());
            request.setAttribute("isNextWeek", isNextWeek);
            request.setAttribute("startOfWeek", startOfWeek);

            request.getRequestDispatcher("/WEB-INF/views/schedule.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/doctors");
        }
    }

    private Map<String, List<TimeSlot>> generateWeeklySchedule(Long doctorId, boolean isNextWeek) throws Exception {
        Map<String, List<TimeSlot>> schedule = new LinkedHashMap<>();
        AppointmentService appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");

        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        if (isNextWeek) {
            startOfWeek = startOfWeek.plusWeeks(1);
        }

        // Получаем записи на выбранную неделю
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            List<Appointment> dayAppointments = appointmentService.getDoctorAppointmentsForDate(doctorId, currentDate);
            appointments.addAll(dayAppointments);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            String dayKey = getDayKey(currentDate.getDayOfWeek());

            List<TimeSlot> daySlots = new ArrayList<>();
            List<String> timeSlots = getTimeSlotsForDoctor(doctorId, currentDate.getDayOfWeek());

            for (String time : timeSlots) {
                boolean isAvailable = isTimeSlotAvailable(appointments, currentDate, time);
                boolean isPast = isTimeSlotPast(currentDate, time);

                String status;
                if (isPast) {
                    status = "past";
                } else if (isAvailable) {
                    status = "available";
                } else {
                    status = "booked";
                }

                daySlots.add(new TimeSlot(time, isAvailable, status, currentDate));
            }

            schedule.put(dayKey, daySlots);
        }

        return schedule;
    }

    // Остальные методы остаются без изменений...
    private List<String> getTimeSlotsForDoctor(Long doctorId, DayOfWeek dayOfWeek) {
        // Расписание по ТЗ для каждого врача
        switch (doctorId.intValue()) {
            case 1: // Эргашев
                if (Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY).contains(dayOfWeek)) {
                    return Arrays.asList("08:00", "09:00", "10:00", "11:00", "12:00", "13:00");
                }
                break;
            case 2: // Валиуллина
                if (dayOfWeek == DayOfWeek.SATURDAY) {
                    return Arrays.asList("08:00", "09:00", "10:00", "11:00", "12:00");
                } else if (Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).contains(dayOfWeek)) {
                    return Arrays.asList("12:00", "13:00", "14:00", "15:00", "16:00");
                } else if (dayOfWeek == DayOfWeek.FRIDAY) {
                    return Arrays.asList("13:00", "14:00", "15:00", "16:00", "17:00");
                }
                break;
            case 3: // Вильданов
                if (Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY).contains(dayOfWeek)) {
                    return Arrays.asList("08:00", "09:00", "10:00", "11:00", "12:00");
                } else if (Arrays.asList(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY).contains(dayOfWeek)) {
                    return Arrays.asList("09:00", "10:00", "11:00");
                }
                break;
            case 4: // Гарифуллина
                if (Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY).contains(dayOfWeek)) {
                    return Arrays.asList("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00");
                }
                break;
        }
        return new ArrayList<>();
    }

    private boolean isTimeSlotAvailable(List<Appointment> appointments, LocalDate date, String time) {
        return appointments.stream()
                .noneMatch(app -> app.getAppointmentDate().equals(date) &&
                        app.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")).equals(time));
    }

    private boolean isTimeSlotPast(LocalDate date, String time) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            return true;
        }
        if (date.equals(today)) {
            LocalTime now = LocalTime.now();
            LocalTime slotTime = LocalTime.parse(time);
            return slotTime.isBefore(now);
        }
        return false;
    }

    private String getDayKey(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "mon";
            case TUESDAY: return "tue";
            case WEDNESDAY: return "wed";
            case THURSDAY: return "thu";
            case FRIDAY: return "fri";
            case SATURDAY: return "sat";
            case SUNDAY: return "sun";
            default: return "";
        }
    }

    private List<String> generateTimeSlots() {
        return Arrays.asList("08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00");
    }

    private List<String> getWeekDays() {
        return Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");
    }

    // Обновленный TimeSlot с датой
    public static class TimeSlot {
        private String time;
        private boolean available;
        private String status;
        private LocalDate date;

        public TimeSlot(String time, boolean available, String status, LocalDate date) {
            this.time = time;
            this.available = available;
            this.status = status;
            this.date = date;
        }

        public String getTime() { return time; }
        public boolean isAvailable() { return available; }
        public String getStatus() { return status; }
        public LocalDate getDate() { return date; }
    }
}