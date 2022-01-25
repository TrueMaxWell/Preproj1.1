package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Util.class);
        UserServiceImpl userService = context.getBean(UserServiceImpl.class);

//        UserServiceImpl userService = new UserServiceImpl();


//        userService.createUsersTable();

//        userService.dropUsersTable();

//        userService.saveUser("Max","Tkach", (byte) 24);
//        userService.saveUser("Anatolii","Marandyuk", (byte) 23);
//        userService.saveUser("Ivan","Ivanov", (byte) 35);
//        userService.saveUser("Evgenii","Volkov", (byte) 29);

//        userService.removeUserById(3);
//

//        userService.getAllUsers().forEach(System.out::println);


//        userService.cleanUsersTable();

//        userService.dropUsersTable();
//        userService.createUsersTable();
//        userService.saveUser("Anatolii","Marandyuk", (byte) 23);
//
//        User user = userService.getAllUsers().get(0);
//
//        if (!"Anatolii".equals(user.getName())
//                || !"Marandyuk".equals(user.getLastName())
//                || (byte) 23 != user.getAge()
//        ) {
//            System.out.println("User был некорректно добавлен в базу данных");
//        }

        try {
            userService.dropUsersTable();
            userService.createUsersTable();
        } catch (Exception e) {
            System.out.println("При тестировании создания таблицы пользователей произошло исключение\n" + e.getMessage());
        }





    }
}
