package org.example.library;

public class FavoriteBook {
    private int userId;
    private int bookId;
    private String title;
    private String summary;
    private String isbn;
    private String imageUrl;
    private int pageCount;
    private String categoryName;

    public FavoriteBook(int userId, int bookId, String title, String summary, String isbn, String imageUrl, int pageCount, String categoryName) {
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.summary = summary;
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.pageCount = pageCount;
        this.categoryName = categoryName;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

}
