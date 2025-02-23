package org.example.library;

public class User {
    private int userId;
    private String userName;
    private String passwordHash;
    private String email;
    private boolean isAdmin = false;

    public User() {
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

//    public void setAdmin(boolean admin) {
//        isAdmin = admin;
//    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
