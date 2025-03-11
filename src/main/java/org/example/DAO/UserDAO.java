package org.example.DAO;

import org.example.library.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String URL = "jdbc:mysql://localhost:3310/db_library";

    public static void createUser(User user) {
        String query = "INSERT INTO `users`(`user_name`, `password_Hash`, `email`, `is_admin`) VALUES (?,?,?,?)";
        try {

            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());

            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.executeUpdate();
            System.out.println("Vartotojas sukurtas");
        } catch (SQLException e) {
            System.out.println("Nepavyko sukurti naujo įrašo DB. Daugiau informacijos: " + e.getMessage());
        }
    }

//    public static User readUser(int userId) {
//        String query = "SELECT * FROM `users` WHERE `user_id` = ?";
//        try {
//            Connection connection = DriverManager.getConnection(URL, "root", "");
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, userId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                String userName = resultSet.getString("user_name");
//                String email = resultSet.getString("email");
//                boolean isAdmin = resultSet.getBoolean("is_admin");
//
//                User user = new User();
//                user.setUserName(userName);
//                user.setEmail(email);
//                user.setAdmin(isAdmin);
//
//                System.out.println("Pavyko gauti įrašą(-us) iš DB");
//                return user;
//            } else {
//                System.out.println("Įrašas nerastas");
//                return null;
//            }
//        } catch (SQLException e) {
//            System.out.println("Nepavyko gauti įrašo iš DB. Daugiau informacijos: " + e.getMessage());
//            return null;
//        }
//    }

    public static boolean updateUser(User user) {
        String query = "UPDATE `users` SET `password_Hash` = ?, `email` = ?, `is_admin` = ? WHERE `user_name` = ?";
        try {
            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());

            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setBoolean(3, user.isAdmin());
            preparedStatement.setString(4, user.getUserName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Nepavyko atnaujinti vartotojo. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(String userName) {
        String query = "DELETE FROM `users` WHERE `user_name` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko ištrinti vartotojo. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

//    public static ArrayList<User> searchUsers() {
//        String query = "SELECT * FROM `users`";
//        ArrayList<User> users = new ArrayList<>();
//        try {
//            Connection connection = DriverManager.getConnection(URL, "root", "");
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//
//            ResultSet resultSet = preparedStatement.executeQuery(query);
//
//            while (resultSet.next()) {
//                int userId = resultSet.getInt("user_id");
//                String userName = resultSet.getString("user_name");
//                String passwordHash = resultSet.getString("password_hash");
//                String email = resultSet.getString("email");
//                boolean isAdmin = resultSet.getBoolean("is_admin");
//                users.add(new User(userId, userName, passwordHash, email, isAdmin));
//            }
//            if (users.isEmpty()) {
//                System.out.println("Vartotojų duomenų bazėje nėra");
//            } else {
//                System.out.println("Atlikta vartotojų paieška duomenų bazėje:");
//            }
//
//            preparedStatement.close();
//            connection.close();
//        } catch (SQLException e) {
//            System.out.println("Įvyko klaida atliekant paiešką: " + e.getMessage());
//        }
//        return users;
//    }

    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT password_hash FROM `users` WHERE BINARY `user_name` = ?";

        try (Connection connection = DriverManager.getConnection(URL, "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password_hash");

                    return BCrypt.checkpw(password, storedHash);
                }
            }
        } catch (SQLException e) {
            System.out.println("Prisijungti nepavyko: " + e.getMessage());
        }
        return false;
    }

    public static User getUser(String userName) {
        User user = null;
        String query = "SELECT * FROM users WHERE user_name = ?";
        try (Connection connection = DriverManager.getConnection(URL, "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String passwordHash = resultSet.getString("password_hash");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                user = new User(userId, userName, passwordHash, email, isAdmin);
                System.out.println("User retrieved with ID: " + userId + ", isAdmin: " + isAdmin); // Debugging
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti vartotojo. Daugiau informacijos: " + e.getMessage());
        }
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `users`";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String passwordHash = resultSet.getString("password_Hash");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                users.add(new User(userName, passwordHash, email, isAdmin));
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti vartotojų sąrašo. Daugiau informacijos: " + e.getMessage());
        }
        return users;
    }

    public static void viewAllUsers() {
        List<User> users = UserDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Nėra registruotų vartotojų.");
        } else {
            System.out.println("Visi registruoti vartotojai:");
            for (User user : users) {
                System.out.println("Vartotojo vardas: " + user.getUserName());
                System.out.println("El.pašto adresas: " + user.getEmail());
                System.out.println("Administratorius: " + (user.isAdmin() ? "Taip" : "Ne"));
                System.out.println("-----------------------------");
            }
        }
    }
}







