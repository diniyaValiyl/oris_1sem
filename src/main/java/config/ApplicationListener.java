package config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import dao.*;
import service.*;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // DAO
        UserDao userDao = new UserDaoImpl();
        DoctorDao doctorDao = new DoctorDaoImpl();
        ReviewDao reviewDao = new ReviewDaoImpl();
        AppointmentDao appointmentDao = new AppointmentDaoImpl();

        // Сервисы с DAO
        UserService userService = new UserServiceImpl(userDao);
        DoctorService doctorService = new DoctorServiceImpl(doctorDao);
        ReviewService reviewService = new ReviewServiceImpl(reviewDao);
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDao);

        // Установка в контекст приложения
        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("doctorService", doctorService);
        sce.getServletContext().setAttribute("reviewService", reviewService);
        sce.getServletContext().setAttribute("appointmentService", appointmentService);
        sce.getServletContext().setAttribute("userDao", userDao);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}