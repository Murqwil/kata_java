package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.text.MessageFormat;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS kata.users " +
                    "(id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255), " +
                    "lastName VARCHAR(255), " +
                    "age TINYINT, " +
                    "PRIMARY KEY (id))").executeUpdate();
            transaction.commit();
            System.out.println("Таблица `users` успешно создана!");
        } catch (Exception e) {
            System.out.println("Не удалось создать таблицу `users` в базе данных `kata`" + e);
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS kata.users").executeUpdate();
            System.out.println("Удалили таблицу `users`");
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Не удалось удалить таблицу `users` в базе данных `kata`" + e);
            e.printStackTrace();
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            System.out.println("Пользователи не сохранены");
        }
    }


    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            System.out.println(MessageFormat.format("Пользователь с id = {0} удален!", id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            System.out.println("Пользователь не был удален по ID");
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM users", User.class);
            List<User> resultList = query.getResultList();
            System.out.println("Найденные пользователи: ");
            resultList.forEach(it -> System.out.println(MessageFormat.format("ID: {0} | Name: {1} | LastName: {2} | Age: {3}", it.getId(), it.getName(), it.getLastName(), it.getAge())));
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при попытке вывода всех пользователей");
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("DELETE FROM users");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при попытке очистить таблицу");
        }
    }
}