package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static Configuration configuration = new Configuration();
    private static SessionFactory sessionFactory;
    private static Connection connection;

    private Util() {
    }

    // реализуйте настройку соеденения с БД
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mysql", "root", "root");
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.setProperty("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/kata");
                configuration.setProperty("hibernate.connection.username","root");
                configuration.setProperty("hibernate.connection.password","root");
                configuration.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL8Dialect");
                configuration.setProperty("hibernate.show_sql","false");
                configuration.setProperty("hibernate.logging.level.org.hibernate","ERROR");
                configuration.addAnnotatedClass(User.class);
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
            }
        }
        return sessionFactory;
    }

    // Закрытие SessionFactory
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}