package org.example.library;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserDAO {
    private static final String URL = "jdbc:mysql://localhost:3310/db_library";

    public static void createUser(User user) {
        String query = "INSERT INTO `users`(`username`, `password_Hash`, `email`, `is_admin`) VALUES (?,?,?,?)";
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
}





