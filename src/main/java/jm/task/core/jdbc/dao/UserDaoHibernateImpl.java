package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {

            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    Transaction transaction = null;
                    try {
                        DatabaseMetaData dbm = connection.getMetaData();
                        ResultSet tables = dbm.getTables(null, "idea", "User", new String[]{"table"});
                        final String sql = "CREATE TABLE user (id int NOT NULL UNIQUE AUTO_INCREMENT, name VARCHAR(255), lastName VARCHAR(255), age int)";
                        if (!tables.next()) {
                            transaction = session.beginTransaction();
                            session.createSQLQuery(sql).executeUpdate();
                            session.getTransaction().commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        assert transaction != null;
                        transaction.rollback();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Таблица не создана");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {

            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    Transaction transaction = null;
                    try {
                        DatabaseMetaData dbm = connection.getMetaData();
                        ResultSet tables = dbm.getTables(null, "idea", "User", new String[]{"table"});
                        final String sql = "DROP Table User";
                        if (tables.next()) {
                            transaction = session.beginTransaction();
                            session.createSQLQuery(sql).executeUpdate();
                            session.getTransaction().commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        assert transaction != null;
                        transaction.rollback();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        User user = new User(name, lastName, age);
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        final String hql = "DELETE from User where id = :userId";
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery(hql).setParameter("userId", id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Таблицы не существует");
            assert transaction != null;
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> users = new ArrayList<>();
        final String hql = "FROM User";
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery(hql).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Таблицы не существует");
            assert transaction != null;
            transaction.rollback();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    Transaction transaction = null;
                    try {
                        DatabaseMetaData dbm = connection.getMetaData();
                        ResultSet tables = dbm.getTables(null, "idea", "User", new String[]{"table"});
                        final String hql = "DELETE from User";
                        if (tables.next()) {
                            transaction = session.beginTransaction();
                            session.createSQLQuery(hql).executeUpdate();
                            session.getTransaction().commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Таблицы не существует");
                        assert transaction != null;
                        transaction.rollback();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Таблицы не существует");
        }
    }
}
