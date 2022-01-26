package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;
    String tableName = "users";

    public UserDaoJDBCImpl() {
        connection = Util.getMySQLConnection();
    }

    public void createUsersTable() {
        try {

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (id BIGINT AUTO_INCREMENT, " +
                    "name VARCHAR(256), lastName VARCHAR(256), age TINYINT (3), PRIMARY KEY (id))");
            statement.close();

            System.out.println("Таблица " + tableName + " была успешно создана.\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void dropUsersTable() {
        try {

            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + tableName);
            statement.close();

            System.out.println("Таблица " + tableName + " была успешно удалена.\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            connection.setAutoCommit(false);
            if (!checkUserExisting(name, "name")) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName +
                        " (name, lastName, age) VALUES (?,?,?)");
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.execute();
                statement.close();
                connection.commit();

                System.out.println("User с именем – " + name + " добавлен в базу данных\n");
            } else {
                System.out.println("Пользователь уже существует.\n");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            System.out.println("Не удалось добавить пользователя.\n");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void removeUserById(long id) {

        try {
            if (checkUserExisting(id, "id")) {
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName +
                        " WHERE id = ?");
                statement.setLong(1, id);
                statement.execute();
                statement.close();

                connection.commit();
                System.out.println("Пользователь под номером " + id + " был успешно удален из базы.\n");
            } else {
                System.out.println("Пользователя с id=" + id + " не существует\n");
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            System.out.println("Не удалось удалить пользователя (id = " + id + ")\n");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }

    }

    private Boolean checkUserExisting(Object value, Object idOrName) {

        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM " + tableName + " WHERE " + idOrName + " = '" + value + "'");
            ResultSet resultSet = statement.getResultSet();
            return resultSet.next();


        } catch (SQLException e) {
            return false;
        }

    }

    public List<User> getAllUsers() {
        try {
            Statement statement = connection.createStatement();
            ResultSet table = statement.executeQuery("SELECT * from " + tableName);
            List<User> allUsers = new ArrayList<>();
            while (table.next()) {
                allUsers.add(new User(table.getString(2),
                        table.getString(3),
                        table.getByte(4)));
            }
            return allUsers;

        } catch (SQLException e) {
            System.out.println("База данных " + tableName + " не существует");
            return null;
        }
    }

    public void cleanUsersTable() {

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + tableName);
            statement.close();
            connection.commit();
            System.out.println("Таблица '" + tableName + "' была очищена");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {

            } finally {
                System.out.println("Не удалось очистить таблицу '" + tableName + "'\n");
            }

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {

            }
        }
    }
}
