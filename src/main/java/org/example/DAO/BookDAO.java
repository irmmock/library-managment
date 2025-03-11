package org.example.DAO;

import org.example.library.Book;
import org.example.library.FavoriteBook;
import org.example.library.Reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDAO {
    private static final String URL = "jdbc:mysql://localhost:3310/db_library";

    public static void createBook(Book book) {
        String query = "INSERT INTO `books`(`title`, `summary`, `isbn`, `image_Url`, `page_count`, `category_id`, `available_copies`) VALUES (?,?,?,?,?,?,?)";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getSummary());
            preparedStatement.setString(3, book.getIsbn());
            preparedStatement.setString(4, book.getImageUrl());
            preparedStatement.setInt(5, book.getPageCount());
            preparedStatement.setInt(6, book.getCategoryId());
            preparedStatement.setInt(7, book.getAvailableCopies());
            preparedStatement.executeUpdate();
            System.out.println("Knyga sukurta DB");
        } catch (SQLException e) {
            System.out.println("Nepavyko sukurti naujo įrašo DB. Daugiau informacijos: " + e.getMessage());
        }
    }

    public static boolean deleteBook(int bookId) {
        String query = "DELETE FROM `books` WHERE `book_id` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko pašalinti knygos iš DB. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateBook(Book book) {
        String query = "UPDATE `books` SET `title` = ?, `summary` = ?, `isbn` = ?, `image_Url` = ?, `page_count` = ?, `category_id` = ?, `available_copies` = ? WHERE `book_id` = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getSummary());
            preparedStatement.setString(3, book.getIsbn());
            preparedStatement.setString(4, book.getImageUrl());
            preparedStatement.setInt(5, book.getPageCount());
            preparedStatement.setInt(6, book.getCategoryId());
            preparedStatement.setInt(7, book.getAvailableCopies());
            preparedStatement.setInt(8, book.getBookId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko atnaujinti knygos informacijos DB. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }


    public static Book getBook(int bookId) {
        String query = "SELECT b.*, c.category_name FROM books b " +
                "JOIN categories c ON b.category_id = c.category_id " +
                "WHERE b.book_id = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("summary"),
                        resultSet.getString("isbn"),
                        resultSet.getString("image_url"),
                        resultSet.getInt("page_count"),
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name"),
                        resultSet.getInt("available_copies")
                );
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti knygos informacijos iš DB. Daugiau informacijos: " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Book> searchBookByTitle(String title) {
        String query = "SELECT b.*, c.category_name FROM books b " +
                "JOIN categories c ON b.category_id = c.category_id " +
                "WHERE b.title LIKE ?";
        ArrayList<Book> books = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(URL, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String bookTitle = resultSet.getString("title");
                String summary = resultSet.getString("summary");
                String isbn = resultSet.getString("isbn");
                String imageUrl = resultSet.getString("image_url");
                int pageCount = resultSet.getInt("page_count");
                int categoryId = resultSet.getInt("category_id");
                String categoryName = resultSet.getString("category_name");
                int availableCopies = resultSet.getInt("available_copies");

                books.add(new Book(bookId, bookTitle, summary, isbn, imageUrl, pageCount, categoryId, categoryName, availableCopies));
            }

            if (books.isEmpty()) {
                System.out.println("Pagal įvestus kriterijus nieko nerasta");
            } else {
                System.out.println("Pavyko atlikti paiešką pagal knygos pavadinimą");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Įvyko klaida atliekant paiešką pagal knygos pavadinimą: " + e.getMessage());
        }
        return books;
    }

    public static boolean reserveBook(int userId, int bookId, Date reservationDate, Date expirationDate) {
        Book book = getBook(bookId);

        if (book != null && book.getAvailableCopies() > 0) {
            String query = "INSERT INTO `reservations`(`user_id`, `book_id`, `reservation_date`, `expiration_date`) VALUES (?,?,?,?)";
            try (Connection connection = DriverManager.getConnection(URL, "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                connection.setAutoCommit(false); // Pradėti transakciją

                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, bookId);
                preparedStatement.setDate(3, new java.sql.Date(reservationDate.getTime()));
                preparedStatement.setDate(4, new java.sql.Date(expirationDate.getTime()));
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    book.setAvailableCopies(book.getAvailableCopies() - 1);
                    updateBook(book);
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                }
            } catch (SQLException e) {
                System.out.println("Nepavyko rezervuoti knygos. Daugiau informacijos: " + e.getMessage());
            }
        } else {
            System.out.println("Knygos rezervacija negalima, visos knygos rezervuotos.");
        }
        return false;
    }

    public static List<Reservation> getReservationsByUser(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, b.title, b.summary FROM reservations r " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.user_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                Date reservationDate = resultSet.getDate("reservation_date");
                Date expirationDate = resultSet.getDate("expiration_date");
                String bookTitle = resultSet.getString("title");
                String bookSummary = resultSet.getString("summary");
                reservations.add(new Reservation(userId, bookId, reservationDate, expirationDate, bookTitle, bookSummary));
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti rezervacijų. Daugiau informacijos: " + e.getMessage());
        }
        return reservations;
    }

    public static boolean addFavoriteBook(int userId, int bookId) {
        String query = "INSERT INTO favorite_books (user_id, book_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Nepavyko pridėti mėgstamos knygos. Daugiau informacijos: " + e.getMessage());
            return false;
        }
    }

    public static List<FavoriteBook> getFavoriteBooksByUser(int userId) {
        List<FavoriteBook> favoriteBooks = new ArrayList<>();
        String query = "SELECT fb.user_id, fb.book_id, b.title, b.summary, b.isbn, b.image_url, b.page_count, c.category_name " +
                "FROM favorite_books fb " +
                "JOIN books b ON fb.book_id = b.book_id " +
                "JOIN categories c ON b.category_id = c.category_id " +
                "WHERE fb.user_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String summary = resultSet.getString("summary");
                String isbn = resultSet.getString("isbn");
                String imageUrl = resultSet.getString("image_url");
                int pageCount = resultSet.getInt("page_count");
                String categoryName = resultSet.getString("category_name");
                favoriteBooks.add(new FavoriteBook(userId, bookId, title, summary, isbn, imageUrl, pageCount, categoryName));
            }
        } catch (SQLException e) {
            System.out.println("Nepavyko gauti mėgstamų knygų. Daugiau informacijos: " + e.getMessage());
        }
        return favoriteBooks;
    }

}