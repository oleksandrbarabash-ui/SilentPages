package com.library.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO для адмін-панелі: містить інформацію про замовлення, книги та клієнта.
 */
public class AdminReservationDto {
    private int id;
    private String userEmail;
    private String userFullName;
    private LocalDate createTime;
    private LocalDate updateTime;
    private String overallStatus;
    private List<ReservationBookItemDto> books;

    public AdminReservationDto(int id, String userEmail, String userFullName,
                               LocalDate createTime, LocalDate updateTime,
                               String overallStatus, List<ReservationBookItemDto> books) {
        this.id = id;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.overallStatus = overallStatus;
        this.books = books;
    }

    // Геттери
    public int getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getUserFullName() { return userFullName; }
    public LocalDate getCreateTime() { return createTime; }
    public LocalDate getUpdateTime() { return updateTime; }
    public String getOverallStatus() { return overallStatus; }
    public List<ReservationBookItemDto> getBooks() { return books; }
}