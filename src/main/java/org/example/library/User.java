package org.example.library;

import org.example.DAO.BookDAO;
import org.example.DAO.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.library.Book.reserveBook;

public class User {
    private int userId;
    private String userName;
    private String passwordHash;
    private String email;
    private boolean isAdmin = false;

    public User() {
    }

    public User(int userId, String userName, String passwordHash, String email) {
        this.userId = userId;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public User(String userName, String passwordHash, String email, boolean isAdmin) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(int userId, String userName, String passwordHash, String email, boolean isAdmin) {
        this.userId = userId;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}' + '\n';
    }

    public static void login() {
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;
        User user = null;

        do {
            System.out.println("Prisijungimas");
            System.out.print("Įveskite vartotojo vardą: ");
            String userName = scanner.nextLine();

            System.out.print("Įveskite vartotojo slaptažodį: ");
            String userPassword = scanner.nextLine();

            isAuthenticated = UserDAO.authenticateUser(userName, userPassword);

            if (isAuthenticated) {
                System.out.println("Sėkmingai prisijungėte!");

                user = UserDAO.getUser(userName);
                System.out.println("User ID after login: " + user.getUserId()); // Debugging
                showUserMenu(user);
            } else {
                System.out.println("Prisijungimas nesėkmingas, blogas vartotojo vardas arba slaptažodis!");
                String retry;
                do {
                    System.out.print("Ar norite bandyti dar kartą? (taip/ne): ");
                    retry = scanner.nextLine();
                    if (!retry.equalsIgnoreCase("taip") && !retry.equalsIgnoreCase("ne")) {
                        System.out.println("Neteisinga įvestis. Prašome įvesti 'taip' arba 'ne'.");
                    }
                } while (!retry.equalsIgnoreCase("taip") && !retry.equalsIgnoreCase("ne"));

                if (retry.equalsIgnoreCase("ne")) {
                    return;
                }
            }
        } while (!isAuthenticated);
    }

    public static void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Registracija");

        String userName;
        do {
            System.out.print("Įveskite vartotojo vardą: ");
            userName = scanner.nextLine();
            if (userName.isEmpty()) {
                System.out.println("Vartotojo vardas negali būti tuščias. Bandykite dar kartą.");
            }
        } while (userName.isEmpty());

        String userPassword;
        do {
            System.out.print("Įveskite vartotojo slaptažodį: ");
            userPassword = scanner.nextLine();
            if (userPassword.isEmpty()) {
                System.out.println("Slaptažodis negali būti tuščias. Bandykite dar kartą.");
            }
        } while (userPassword.isEmpty());

        String email;
        do {
            System.out.print("Įveskite el.pašto adresą: ");
            email = scanner.nextLine();
            if (email.isEmpty()) {
                System.out.println("El.pašto adresas negali būti tuščias. Bandykite dar kartą.");
            }
        } while (email.isEmpty());

        User user = new User(userName, userPassword, email, false);
        UserDAO.createUser(user);

        System.out.println("Ką norėtumėte daryti toliau?");
        System.out.println("1. Prisijungti");
        System.out.println("0. Grįžti į pagrindinį meniu");
        System.out.println("9. Išeiti");
        showAdditionalMenu();
    }

    public static void showAdditionalMenu() {
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        boolean exit = false;
        while (!exit) {
            switch (choice) {
                case "1":
                    User.login();
                    break;
                case "0":
                    exit = true;
                    showMainMenu();
                    break;
                case "9":
                    exit = true;
                    System.out.println("Ačiū, kad naudojotės mūsų sistema!");
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                    break;
            }
        }
    }

    public static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Sveiki! pasirinkite:");
            System.out.println("1. Prisijungti");
            System.out.println("2. Registruotis");
            System.out.println("9. Išeiti");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "9":
                    System.out.println("Ačiū, kad naudojotės mūsų sistema!");
                    exit = true;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                    break;
            }
        }
    }

    public static void showUserMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            if (user.isAdmin()) {
                System.out.println("Sveiki, administratoriau!");
                System.out.println("1. Vartotojų valdymas");
                System.out.println("2. Knygų kategorijų valdymas");
                System.out.println("3. Knygų valdymas");
                System.out.println("0. Grįžti į pagrindinį meniu");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        showUsersManagmentMenu(user);
                        break;
                    case "2":
                        Category.showBookCategoryMenu(user);
                        break;
                    case "3":
                        Book.showBookManagmentMenu(user);
                        break;
                    case "0":
                        showMainMenu();
                        exit = true;
                        break;
                    default:
                        System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                        break;
                }
            } else {
                System.out.println("Sveiki, vartotojau!");
                System.out.println("1. Knygų paieška");
                System.out.println("2. Rezervuoti knygą");
                System.out.println("3. Peržiūrėti rezervacijas");
                System.out.println("4. Pažymėti knygą kaip mėgstamą");
                System.out.println("5. Mėgstamų knygų sąrašas");
                System.out.println("0. Grįžti į pagrindinį meniu");
                System.out.println("9. Išeiti");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        System.out.println("Įveskite knygos pavadinimą:");
                        String title = scanner.nextLine();
                        ArrayList<Book> books = BookDAO.searchBookByTitle(title);

                        if (books.isEmpty()) {
                            System.out.println("Pagal įvestus kriterijus nieko nerasta.");
                        } else {
                            System.out.println("Rastos knygos:");
                            for (Book book : books) {
                                System.out.println(book);
                            }
                        }
                        break;
                    case "2":
                        reserveBook(user);
                        break;
                    case "3":
                        user.showReservations();
                        break;
                    case "4":
                        System.out.print("Įveskite knygos ID, kurią norite pridėti į mėgstamų knygų sąrašą: ");
                        int bookId = scanner.nextInt();
                        scanner.nextLine();
                        user.addFavoriteBook(bookId);
                        break;
                    case "5":
                        user.showFavoriteBooks();
                        break;
                    case "0":
                        showMainMenu();
                        exit = true;
                        break;
                    case "9":
                        System.out.println("Ačiū, kad naudojotės mūsų sistema!");
                        exit = true;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                        break;
                }
            }
        }
    }

    public static void showUsersManagmentMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Vartotojų valdymas");
            System.out.println("1. Registruotų vartotojų sąrašas");
            System.out.println("2. Pridėti naują vartotoją");
            System.out.println("3. Redaguoti vartotoją");
            System.out.println("4. Ištrinti vartotoją");
            System.out.println("0. Grįžti į pagrindinį meniu");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    UserDAO.viewAllUsers();
                    break;
                case "2":
                    addUser();
                    break;
                case "3":
                    editUser();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "0":
                    exit = true;
                    showUserMenu(user);
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas. Bandykite dar kartą.");
                    break;
            }
        }
    }

    public static void addUser() {
        Scanner scanner = new Scanner(System.in);
        String userName;
        boolean userNameExists;

        do {
            System.out.print("Įveskite naujo vartotojo vardą: ");
            userName = scanner.nextLine();

            userNameExists = UserDAO.getUser(userName) != null;
            if (userNameExists) {
                System.out.println("Vartotojo vardas jau panaudotas. Pasirinkite kitą vardą.");
            }
        } while (userNameExists);

        System.out.print("Įveskite naują vartotojo slaptažodį: ");
        String userPassword = scanner.nextLine();

        System.out.print("Įveskite naują vartotojo el.pašto adresą: ");
        String email = scanner.nextLine();

        String isAdminInput;
        boolean isAdmin = false;
        do {
            System.out.print("Ar vartotojas yra administratorius? (taip/ne): ");
            isAdminInput = scanner.nextLine();
            if (isAdminInput.equalsIgnoreCase("taip")) {
                isAdmin = true;
            } else if (isAdminInput.equalsIgnoreCase("ne")) {
                isAdmin = false;
            } else {
                System.out.println("Neteisingai įvesta. Prašome įvesti 'taip' arba 'ne'.");
            }
        } while (!isAdminInput.equalsIgnoreCase("taip") && !isAdminInput.equalsIgnoreCase("ne"));

        User user = new User(userName, userPassword, email, isAdmin);
        UserDAO.createUser(user);
        System.out.println("Naujas vartotojas pridėtas sėkmingai!");
    }

    public static void deleteUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite vartotojo vardą, kurį norite ištrinti: ");
        String userName = scanner.nextLine();

        User user = UserDAO.getUser(userName);
        if (user != null) {
            System.out.println("Vartotojo informacija:");
            System.out.println("Vartotojo vardas: " + user.getUserName());
            System.out.println("El.pašto adresas: " + user.getEmail());
            System.out.println("Administratorius: " + (user.isAdmin() ? "Taip" : "Ne"));

            String confirmation;
            do {
                System.out.print("Ar tikrai norite ištrinti šį vartotoją? (taip/ne): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne")) {
                    System.out.println("Neteisinga įvestis. Prašome įvesti 'taip' arba 'ne'.");
                }
            } while (!confirmation.equalsIgnoreCase("taip") && !confirmation.equalsIgnoreCase("ne"));

            if (confirmation.equalsIgnoreCase("taip")) {
                boolean isDeleted = UserDAO.deleteUser(userName);
                if (isDeleted) {
                    System.out.println("Vartotojas sėkmingai ištrintas!");
                } else {
                    System.out.println("Nepavyko ištrinti vartotojo.");
                }
            } else {
                System.out.println("Vartotojo ištrynimas atšauktas.");
            }
        } else {
            System.out.println("Vartotojas nerastas.");
        }
    }

    public static void editUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Įveskite vartotojo vardą, kurį norite redaguoti: ");
        String userName = scanner.nextLine();

        User user = UserDAO.getUser(userName);
        if (user != null) {
            System.out.println("Vartotojo informacija:");
            System.out.println("Vartotojo vardas: " + user.getUserName());
            System.out.println("El.pašto adresas: " + user.getEmail());
            System.out.println("Administratorius: " + (user.isAdmin() ? "Taip" : "Ne"));

            System.out.print("Įveskite naują slaptažodį (palikite tuščią, jei nenorite keisti): ");
            String newPassword = scanner.nextLine();
            if (!newPassword.isEmpty()) {
                user.setPasswordHash(newPassword);
            }

            System.out.print("Įveskite naują el.pašto adresą (palikite tuščią, jei nenorite keisti): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.isEmpty()) {
                user.setEmail(newEmail);
            }

            String isAdminInput;
            do {
                System.out.print("Ar vartotojas yra administratorius? (taip/ne): ");
                isAdminInput = scanner.nextLine();
                if (isAdminInput.equalsIgnoreCase("taip")) {
                    user.setAdmin(true);
                } else if (isAdminInput.equalsIgnoreCase("ne")) {
                    user.setAdmin(false);
                } else {
                    System.out.println("Neteisinga įvestis. Prašome įvesti 'taip' arba 'ne'.");
                }
            } while (!isAdminInput.equalsIgnoreCase("taip") && !isAdminInput.equalsIgnoreCase("ne"));

            boolean isUpdated = UserDAO.updateUser(user);
            if (isUpdated) {
                System.out.println("Vartotojas sėkmingai atnaujintas!");
            } else {
                System.out.println("Nepavyko atnaujinti vartotojo.");
            }
        } else {
            System.out.println("Vartotojas nerastas.");
        }
    }

    public void showReservations() {
        List<Reservation> reservations = BookDAO.getReservationsByUser(userId);
        if (reservations.isEmpty()) {
            System.out.println("Jūs neturite rezervuotų knygų.");
        } else {
            System.out.println("Jūsų rezervuotos knygos:");
            for (Reservation reservation : reservations) {
                System.out.println("Knygos ID: " + reservation.getBookId() +
                        ", Pavadinimas: " + reservation.getBookTitle() +
                        ", Santrauka: " + reservation.getBookSummary() +
                        ", Rezervacijos data: " + reservation.getReservationDate() +
                        ", Galiojimo data: " + reservation.getExpirationDate());
            }
        }
    }

    public void addFavoriteBook(int bookId) {
        boolean isAdded = BookDAO.addFavoriteBook(userId, bookId);
        if (isAdded) {
            System.out.println("Knyga sėkmingai pridėta į mėgstamų knygų sąrašą!");
        } else {
            System.out.println("Nepavyko pridėti knygos į mėgstamų knygų sąrašą.");
        }
    }

    public void showFavoriteBooks() {
        List<FavoriteBook> favoriteBooks = BookDAO.getFavoriteBooksByUser(userId);
        if (favoriteBooks.isEmpty()) {
            System.out.println("Jūs neturite mėgstamų knygų.");
        } else {
            System.out.println("Jūsų mėgstamos knygos:");
            for (FavoriteBook favoriteBook : favoriteBooks) {
                System.out.println("Knygos ID: " + favoriteBook.getBookId() +
                        ", Pavadinimas: " + favoriteBook.getTitle() +
                        ", Santrauka: " + favoriteBook.getSummary() +
                        ", ISBN: " + favoriteBook.getIsbn() +
                        ", Paveikslėlio URL: " + favoriteBook.getImageUrl() +
                        ", Puslapių skaičius: " + favoriteBook.getPageCount() +
                        ", Kategorija: " + favoriteBook.getCategoryName());
            }
        }
    }

}

