package org.example.library;

import java.util.Date;

public class Reservation {
    private int userId;
    private int bookId;
    private Date reservationDate;
    private Date expirationDate;
    private String bookTitle;
    private String bookSummary;

    public Reservation(int userId, int bookId, Date reservationDate, Date expirationDate, String bookTitle, String bookSummary) {
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.bookTitle = bookTitle;
        this.bookSummary = bookSummary;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookSummary() {
        return bookSummary;
    }
}
