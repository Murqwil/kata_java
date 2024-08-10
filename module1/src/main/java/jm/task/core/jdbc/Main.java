package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;


public class  Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserService userService = new UserServiceImpl();


        Util.getConnection();
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
        }

            userService.createUsersTable();


            userService.saveUser("Name1", "LastName1", (byte) 20);
            userService.saveUser("Name2", "LastName2", (byte) 25);
            userService.saveUser("Name3", "LastName3", (byte) 31);
            userService.saveUser("Name4", "LastName4", (byte) 38);

            userService.getAllUsers();

            userService.removeUserById(1);

            userService.cleanUsersTable();

            userService.dropUsersTable();
        }
    }

