package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS kata.user (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "Name VARCHAR(50) NOT NULL," +
            "LastName VARCHAR(50) NOT NULL," +
            "Age INT NOT NULL" +
            ")";

    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS kata.user";

    private static final String INSERT_USER_QUERY = "INSERT INTO `kata`.`user` (`Name`, `LastName`, `Age`) VALUES (?, ?, ?)";

    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM `kata`.`user` WHERE `id` = ?";

    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM kata.user";

    private static final String CLEAN_USERS_TABLE_QUERY = "DELETE FROM `kata`.`user`";

    private static final Connection connection = Util.getConnection();



    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
            System.out.println("Таблица 'user' успешно создана в базе данных 'kata'.");
        } catch (SQLException e) {
            System.out.println("Не удалось создать таблицу 'user' в базе данных 'kata'.");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE_QUERY);
            System.out.println("Таблица 'user' успешно удалена из базы данных 'kata'.");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении таблицы 'user' из базы данных 'kata'.");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(INSERT_USER_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Пользователи успешно добавлены.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID_QUERY)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            System.out.println("Пользователь с ID " + id + " был удален.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя с ID " + id, e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("Name");
                String lastName = resultSet.getString("LastName");
                byte age = resultSet.getByte("Age");
                User user = new User(id, name, lastName, age);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка пользователей: " + e.getMessage());
        }

        users.forEach(System.out::println);
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_USERS_TABLE_QUERY);
            System.out.println("Произведена очистка таблицы 'user'.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы 'user'.", e);
        }
    }
}
