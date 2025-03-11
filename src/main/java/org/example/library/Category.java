package org.example.library;

import org.example.DAO.CategoryDAO;

import java.util.Scanner;


public class Category {
    private int categoryId;
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(){
    }

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public Category(int categoryId, String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static void addBookCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite naujos knygų kategorijos pavadinimą: ");
        String categoryName = scanner.nextLine();

        if (CategoryDAO.getBookCategory(categoryName) != null) {
            System.out.println("Kategorija jau egzistuoja. Įveskite kitą pavadinimą.");
            return;
        }

        CategoryDAO.createBookCategory(categoryName);
        System.out.println("Knygų kategorija pridėta sėkmingai!");
    }

    public static void deleteBookCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite kategorijos pavadinimą, kurią norite ištrinti: ");
        String categoryName = scanner.nextLine();

        if (CategoryDAO.getBookCategory(categoryName) != null) {
            System.out.println("Kategorijos informacija:");
            System.out.println("Kategorijos pavadinimas: " + categoryName);

            String confirmation;
            do {
                System.out.print("Ar tikrai norite ištrinti šią kategoriją? (taip/ne): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne")) {
                    System.out.println("Neteisingai įvesta. Prašome įvesti 'taip' arba 'ne'.");
                }
            } while (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne"));

            if (confirmation.equalsIgnoreCase("taip")) {
                boolean isDeleted = CategoryDAO.deleteCategory(categoryName);
                if (isDeleted) {
                    System.out.println("Kategorija sėkmingai ištrinta!");
                } else {
                    System.out.println("Nepavyko ištrinti kategorijos.");
                }
            } else {
                System.out.println("Kategorijos ištrynimas atšauktas.");
            }
        } else {
            System.out.println("Kategorija nerasta.");
        }
    }

    public static void updateBookCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite esamą kategorijos pavadinimą: ");
        String oldCategoryName = scanner.nextLine();

        // Patikrinti, ar kategorija egzistuoja
        if (CategoryDAO.getBookCategory(oldCategoryName) != null) {
            System.out.print("Įveskite naują kategorijos pavadinimą: ");
            String newCategoryName = scanner.nextLine();

            boolean isUpdated = CategoryDAO.updateBookCategory(oldCategoryName, newCategoryName);
            if (isUpdated) {
                System.out.println("Kategorija sėkmingai atnaujinta!");
            } else {
                System.out.println("Nepavyko atnaujinti kategorijos.");
            }
        } else {
            System.out.println("Kategorija nerasta.");
        }
    }

    public static void showBookCategoryMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Knygų kategorijų valdymas");
            System.out.println("1. Peržiūrėti visas knygų kategorijas");
            System.out.println("2. Sukurti knygų kategoriją");
            System.out.println("3. Ištrinti knygų kategoriją");
            System.out.println("4. Redaguoti knygų kategoriją");
            System.out.println("0. Grįžti į pagrindinį meniu");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    CategoryDAO.viewAllCategories();
                    break;
                case "2":
                    addBookCategory();
                    break;
                case "3":
                    deleteBookCategory();
                    break;
                case "4":
                    updateBookCategory();
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


}
