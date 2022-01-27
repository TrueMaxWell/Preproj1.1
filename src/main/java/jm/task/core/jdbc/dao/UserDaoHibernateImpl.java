package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository("HibernateDao")
public class UserDaoHibernateImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;
    private Session session;

    @PostConstruct
    public void init() {
        session = sessionFactory.openSession();
    }

    String entityName = "User";


    @Override
    public void createUsersTable() {
        session.beginTransaction();
        session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT, " +
                "name VARCHAR(256), lastName VARCHAR(256), age TINYINT (3), PRIMARY KEY (id))").executeUpdate();
        session.getTransaction().commit();
        System.out.println("Таблица " + entityName + " была успешно создана.\n");
    }

    @Override
    public void dropUsersTable() {
        session.beginTransaction();
        session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
        session.getTransaction().commit();

        System.out.println("Таблица users была успешно удалена.\n");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        session.beginTransaction();
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        System.out.println("User с именем – " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.delete(user);
        session.getTransaction().commit();
        System.out.println("\nПользователь под номером " + id + " был успешно удален из базы.\n");
    }

    @Override
    public List<User> getAllUsers() {
        session.beginTransaction();
        Query query = session.createQuery("FROM " + entityName);
        ArrayList<User> result = (ArrayList<User>) query.list();
        session.getTransaction().commit();

        return result;
    }

    @Override
    public void cleanUsersTable() {
        session.beginTransaction();
        Query query = session.createQuery("delete from " + entityName);
        query.executeUpdate();
        session.getTransaction().commit();
        System.out.println("\nТаблица '" + entityName + "' была очищена\n");
    }
}
