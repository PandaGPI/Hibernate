package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserServiceImpl userService = new UserServiceImpl();
        userService.dropUsersTable();
        userService.createUsersTable();
        userService.saveUser("Pirogov", "Grigory", (byte) 34);
        userService.saveUser("Pirogov", "Grigory", (byte) 36);
        userService.saveUser("Ivanov", "Ivan", (byte) 22);
        userService.saveUser("Petrov", "Petr", (byte) 53);
        userService.saveUser("Java", "Mentor", (byte) 8);
        userService.cleanUsersTable();
        System.out.println(userService.getAllUsers());
//        userService.cleanUsersTable();
        userService.dropUsersTable();


//        userService.removeUserById(4);


    }
}
