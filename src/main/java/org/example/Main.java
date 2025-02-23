package org.example;

import org.example.library.User;
import org.example.library.UserDAO;

public class Main {
    public static void main(String[] args) {
        System.out.println("Biblioteka v0.1");

        User userObj = new User();
        // String userName, String passwordHash, String email, boolean isAdmin
        User user1 = new User("Admin", "slaptazodis", "admin@admin.lt", true);

        System.out.println(user1);
        UserDAO.createUser(user1);

        User user2 = new User("user", "slaptazodis2", "user@user.lt", false);
        System.out.println(user2);
        UserDAO.createUser(user2);
    }
}