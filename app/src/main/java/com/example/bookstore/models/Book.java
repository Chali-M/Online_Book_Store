package com.example.bookstore.models;

public class Book {
    private String bookName;
    private String bookId;
    private String bookAuthor;
    private String bookPrice;
    private String bookCategory;
    private String bookUrl;
    private String firebaseBookId;

    public Book() {
    }

    public Book(String bookName, String bookId, String bookAuthor, String bookPrice, String bookCategory, String bookUrl, String firebaseBookId) {
        this.bookName = bookName;
        this.bookId = bookId;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookCategory = bookCategory;
        this.bookUrl = bookUrl;
        this.firebaseBookId = firebaseBookId;
    }

    public String getFirebaseBookId() {
        return firebaseBookId;
    }

    public void setFirebaseBookId(String firebaseBookId) {
        this.firebaseBookId = firebaseBookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
}
