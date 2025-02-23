package org.example.library;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;

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

    public static User readUsers(int userId){
        String query = "SELECT * FROM `users` WHERE `user_id` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userName = resultSet.getString("username");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");

                User user = new User();
                user.setUserName(userName);
                user.setEmail(email);
                user.setAdmin(isAdmin);

                System.out.println("Pavyko gauti įrašą(-us) iš DB");
                return user;
            } else {
                System.out.println("Įrašas nerastas");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti įrašo iš DB. Daugiau informacijos: " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<User> searchUsers() {
        String query = "SELECT * FROM `users`";
        ArrayList<User> users = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Duomenų bazė grąžina vykdant įrašų paiešką resultSet
            // reultSet tai neapdirbtas formatas RAW
            // grąžina sąrašą panašų į map, kur raktas - stulpelio pavadinimas, reikšmė - konkreti reikšmė (pagal kurią ieškom)
            ResultSet resultSet = preparedStatement.executeQuery(query);

            // pereiti per resultSet sąrašą naudojam ciklą while
            while (resultSet.next()) {
                // pagal raktą paimama reikšmė
                // pvz. stulpelis - biz_id, reikšmė - konkretaus įrašo id.
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("username");
                String passwordHash = resultSet.getString("password_hash");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");

                // sukuriama airport klasės objektas pagal gautu duomenis ir įdedamas į airports sąrašą.
                users.add(new User(userId, userName, passwordHash, email, isAdmin));

            }
            if (users.isEmpty()){
                System.out.println("Vartotojų duomenų bazėje nėra");
            } else {
                System.out.println("Atlikta vartotojų paieška duomenų bazėje:");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Įvyko klaida atliekant paiešką: " + e.getMessage());
        }
        return users;
    }

}





