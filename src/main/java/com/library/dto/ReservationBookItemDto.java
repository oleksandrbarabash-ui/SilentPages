package com.library.dto;

/**
 * DTO для відображення конкретної книги всередині бронювання.
 */
public class ReservationBookItemDto {
    private int bookId;
    private String name;
    private String author;
    private String statusName; // Окремий статус саме цієї книги

    public ReservationBookItemDto(int bookId, String name, String author, String statusName) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.statusName = statusName;
    }

    // Геттери
    public int getBookId() { return bookId; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getStatusName() { return statusName; }
}