package com.library.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Головний DTO для відображення бронювання (шапка + список книг).
 */
public class ReservationDto {
    private int id;
    private LocalDate createTime;
    private String overallStatus;
    private List<ReservationBookItemDto> books;

    public ReservationDto(int id, LocalDate createTime, String overallStatus, List<ReservationBookItemDto> books) {
        this.id = id;
        this.createTime = createTime;
        this.overallStatus = overallStatus;
        this.books = books;
    }

    // Геттери
    public int getId() { return id; }
    public LocalDate getCreateTime() { return createTime; }
    public String getOverallStatus() { return overallStatus; }
    public List<ReservationBookItemDto> getBooks() { return books; }
}