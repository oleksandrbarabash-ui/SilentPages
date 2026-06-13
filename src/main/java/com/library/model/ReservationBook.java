package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_book")
public class ReservationBook {

    @EmbeddedId
    private ReservationBookId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    @Column(name = "date_of_issue")
    private LocalDate dateOfIssue;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "reservation_book_status_id", nullable = false)
    private ReservationBookStatus status;

    @Column(name = "email_warning_sent", nullable = false)
    private boolean emailWarningSent = false;

    @Column(name = "email_overdue_sent", nullable = false)
    private boolean emailOverdueSent = false;

    public ReservationBook() {}

    public ReservationBook(Reservation reservation, Book book, ReservationBookStatus status) {
        this.id = new ReservationBookId(book.getId(), reservation.getId());
        this.reservation = reservation;
        this.book = book;
        this.status = status;
        
    }
    // Геттери та сеттери
    public ReservationBookId getId() { return id; }
    public Book getBook() { return book; }
    public Reservation getReservation() { return reservation; }
    public LocalDate getDateOfIssue() { return dateOfIssue; }
    public LocalDate getReturnDate() { return returnDate; }
    public LocalDate getEndDate() { return endDate; }
    public ReservationBookStatus getStatus() { return status; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public void setStatus(ReservationBookStatus status) {
        this.status = status;
    }
    public boolean isEmailWarningSent() { return emailWarningSent; }
    public void setEmailWarningSent(boolean emailWarningSent) { this.emailWarningSent = emailWarningSent; }

    public boolean isEmailOverdueSent() { return emailOverdueSent; }
    public void setEmailOverdueSent(boolean emailOverdueSent) { this.emailOverdueSent = emailOverdueSent; }
}