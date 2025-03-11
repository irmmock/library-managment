package org.example.library;

import org.example.DAO.BookDAO;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Book extends Category {
    private int bookId;
    private String title;
    private String summary;
    private String isbn;
    private String imageUrl;
    private int pageCount;
    private int categoryId;
    private int availableCopies;

    public Book() {
    }

    public Book(String title, String summary, String isbn, String imageUrl, int pageCount, int categoryId, String categoryName, int availableCopies) {
        super(categoryId, categoryName);
        this.title = title;
        this.summary = summary;
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.pageCount = pageCount;
        this.availableCopies = availableCopies;
    }

    public Book(int bookId, String title, String summary, String isbn, String imageUrl, int pageCount, int categoryId, String categoryName, int availableCopies) {
        super(categoryId, categoryName);
        this.bookId = bookId;
        this.title = title;
        this.summary = summary;
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.pageCount = pageCount;
        this.availableCopies = availableCopies;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return "Knyga{" +
                "ID=" + bookId +
                ", Pavadinimas='" + title + '\'' +
                ", Santrauka='" + summary + '\'' +
                ", ISBN='" + isbn + '\'' +
                ", Paveikslėlio URL='" + imageUrl + '\'' +
                ", Puslapių skaičius=" + pageCount +
                ", Kategorijos ID=" + categoryId +
                ", Turimų kopijų skaičius=" + availableCopies +
                '}';
    }

    public static void addBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite knygos pavadinimą: ");
        String title = scanner.nextLine();

        System.out.print("Įveskite knygos santrauką: ");
        String summary = scanner.nextLine();

        System.out.print("Įveskite knygos ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Įveskite knygos paveikslėlio URL: ");
        String imageUrl = scanner.nextLine();

        System.out.print("Įveskite knygos puslapių skaičių: ");
        int pageCount = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Įveskite kategorijos ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Įveskite kategorijos pavadinimą: ");
        String categoryName = scanner.nextLine();

        System.out.print("Įveskite turimų kopijų skaičių: ");
        int availableCopies = scanner.nextInt();
        scanner.nextLine();

        Book book = new Book(title, summary, isbn, imageUrl, pageCount, categoryId, categoryName, availableCopies);
        BookDAO.createBook(book);

        System.out.println("Nauja knyga pridėta sėkmingai!");
    }

    public static void deleteBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite knygos ID, kurią norite ištrinti: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        Book book = BookDAO.getBook(bookId);
        if (book != null) {
            System.out.println("Knygos informacija:");
            System.out.println(book);

            String confirmation;
            do {
                System.out.print("Ar tikrai norite ištrinti šią knygą? (taip/ne): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne")) {
                    System.out.println("Neteisingai įvesta. Prašome įvesti 'taip' arba 'ne'.");
                }
            } while (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne"));

            if (confirmation.equalsIgnoreCase("taip")) {
                boolean isDeleted = BookDAO.deleteBook(bookId);
                if (isDeleted) {
                    System.out.println("Knyga sėkmingai ištrinta!");
                } else {
                    System.out.println("Nepavyko ištrinti knygos.");
                }
            } else {
                System.out.println("Knygos ištrynimas atšauktas.");
            }
        } else {
            System.out.println("Knyga nerasta.");
        }
    }

    public static void editBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite knygos ID, kurią norite redaguoti: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        Book book = BookDAO.getBook(bookId);
        if (book != null) {
            System.out.println("Knygos informacija:");
            System.out.println("Knygos ID: " + book.getBookId());
            System.out.println("Pavadinimas: " + book.getTitle());
            System.out.println("Santrauka: " + book.getSummary());
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Paveikslėlio URL: " + book.getImageUrl());
            System.out.println("Puslapių skaičius: " + book.getPageCount());
            System.out.println("Kategorijos ID: " + book.getCategoryId());
            System.out.println("Turimų kopijų skaičius: " + book.getAvailableCopies());

            System.out.print("Įveskite naują pavadinimą (palikite tuščią, jei nenorite keisti): ");
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) {
                book.setTitle(newTitle);
            }

            System.out.print("Įveskite naują santrauką (palikite tuščią, jei nenorite keisti): ");
            String newSummary = scanner.nextLine();
            if (!newSummary.isEmpty()) {
                book.setSummary(newSummary);
            }

            System.out.print("Įveskite naują ISBN (palikite tuščią, jei nenorite keisti): ");
            String newIsbn = scanner.nextLine();
            if (!newIsbn.isEmpty()) {
                book.setIsbn(newIsbn);
            }

            System.out.print("Įveskite naują paveikslėlio URL (palikite tuščią, jei nenorite keisti): ");
            String newImageUrl = scanner.nextLine();
            if (!newImageUrl.isEmpty()) {
                book.setImageUrl(newImageUrl);
            }

            System.out.print("Įveskite naują puslapių skaičių (palikite tuščią, jei nenorite keisti): ");
            String newPageCountStr = scanner.nextLine();
            if (!newPageCountStr.isEmpty()) {
                int newPageCount = Integer.parseInt(newPageCountStr);
                book.setPageCount(newPageCount);
            }

            System.out.print("Įveskite naują kategorijos ID (palikite tuščią, jei nenorite keisti): ");
            String newCategoryIdStr = scanner.nextLine();
            if (!newCategoryIdStr.isEmpty()) {
                int newCategoryId = Integer.parseInt(newCategoryIdStr);
                book.setCategoryId(newCategoryId);
            }

            System.out.print("Įveskite naują turimų kopijų skaičių (palikite tuščią, jei nenorite keisti): ");
            String newAvailableCopiesStr = scanner.nextLine();
            if (!newAvailableCopiesStr.isEmpty()) {
                int newAvailableCopies = Integer.parseInt(newAvailableCopiesStr);
                book.setAvailableCopies(newAvailableCopies);
            }

            BookDAO.updateBook(book);
            System.out.println("Knyga sėkmingai atnaujinta!");
        } else {
            System.out.println("Knyga nerasta.");
        }
    }

    public static void showBookManagmentMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Knygų valdymas");
            System.out.println("1. Pridėti knygą");
            System.out.println("2. Redaguoti knygą");
            System.out.println("3. Pašalinti knygą");
            System.out.println("0. Grįžti į pagrindinį meniu");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addBook();
                    break;
                case "2":
                    editBook();
                    break;
                case "3":
                    deleteBook();
                    break;
                case "0":
                    exit = true;
                    User.showUserMenu(user);
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                    break;
            }
        }
    }

    public static void reserveBook(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Įveskite knygos ID, kurią norite rezervuoti:");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        Date reservationDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservationDate);
        calendar.add(Calendar.DAY_OF_MONTH, 14); // Rezervacija galioja 14 dienų
        Date expirationDate = calendar.getTime();

        boolean isReserved = BookDAO.reserveBook(user.getUserId(), bookId, reservationDate, expirationDate);
        if (isReserved) {
            System.out.println("Knyga sėkmingai rezervuota!");
        } else {
            System.out.println("Nepavyko rezervuoti knygos.");
        }
    }

}