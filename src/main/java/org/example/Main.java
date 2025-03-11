package org.example;

import org.example.DAO.UserDAO;
import org.example.library.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("Biblioteka v1.0");

//        String userName, String passwordHash, String email, boolean isAdmin
//        User user1 = new User("Admin", "admin", "admin@admin.lt", true);
//
//        System.out.println(user1);
//        UserDAO.createUser(user1);


        User.showMainMenu();

    }
}