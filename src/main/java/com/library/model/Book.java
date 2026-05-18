package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String author;
    private String language;
    private int pages;

    @ManyToOne
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book (){}

    public Book(String name,
                String author,
                String language,
                int pages,
                BookStatus bookStatus,
                Genre genre) {
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.bookStatus = bookStatus;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    public BookStatus getBookStatus() {
        return bookStatus;
    }
    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

}
