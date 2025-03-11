package org.example.DAO;

import org.example.library.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String URL = "jdbc:mysql://localhost:3310/db_library";

    public static String getBookCategory(String categoryName) {
        String query = "SELECT * FROM `categories` WHERE `category_name` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("category_name");
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti kategorijos informacijos. Daugiau informacijos: " + e.getMessage());
        }
        return null;
    }

    public static void createBookCategory(String categoryName) {
        String query = "INSERT INTO `categories`(`category_name`) VALUES (?)";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, categoryName);
            preparedStatement.executeUpdate();
            System.out.println("Knygų kategorija pridėta į DB sėkmingai!");
        } catch (SQLException e) {
            System.out.println("Nepavyko pridėti knygų kategorijos į DB. Daugiau informacijos: " + e.getMessage());
        }
    }

    public static boolean updateBookCategory(String oldCategoryName, String newCategoryName) {
        String query = "UPDATE `categories` SET `category_name` = ? WHERE `category_name` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newCategoryName);
            preparedStatement.setString(2, oldCategoryName);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko atnaujinti kategorijos. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteCategory(String categoryName) {
        String query = "DELETE FROM `categories` WHERE `category_name` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, categoryName);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko ištrinti kategorijos. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM `categories`";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                categories.add(new Category(categoryName));
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti kategorijų sąrašo. Daugiau informacijos: " + e.getMessage());
        }
        return categories;
    }

    public static void viewAllCategories() {
        List<Category> categories = getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("Nėra knygų kategorijų.");
        } else {
            System.out.println("Visos knygų kategorijos:");
            for (Category category : categories) {
                System.out.println("Kategorija: " + category.getCategoryName());
                System.out.println("-----------------------------");
            }
        }
    }
}