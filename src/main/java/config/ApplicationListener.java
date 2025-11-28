package config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import dao.*;
import service.*;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // Инициализация DAO
        UserDao userDao = new UserDaoImpl();
        DoctorDao doctorDao = new DoctorDaoImpl();
        ReviewDao reviewDao = new ReviewDaoImpl();
        AppointmentDao appointmentDao = new AppointmentDaoImpl();

        // Инициализация сервисов
        UserService userService = new UserServiceImpl(userDao);
        DoctorService doctorService = new DoctorServiceImpl(doctorDao);
        ReviewService reviewService = new ReviewServiceImpl(reviewDao);
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDao);

        // Установка в контекст
        context.setAttribute("userService", userService);
        context.setAttribute("doctorService", doctorService);
        context.setAttribute("reviewService", reviewService);
        context.setAttribute("appointmentService", appointmentService);
    }
}