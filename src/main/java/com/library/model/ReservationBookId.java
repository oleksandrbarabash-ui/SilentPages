package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservationBookId implements Serializable {

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "reservation_id")
    private int reservationId;

    public ReservationBookId() {}

    public ReservationBookId(int bookId, int reservationId) {
        this.bookId = bookId;
        this.reservationId = reservationId;
    }

    // Геттери, equals та hashCode обов'язкові для складених ключів JPA
    public int getBookId() { return bookId; }
    public int getReservationId() { return reservationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationBookId objections = (ReservationBookId) o;
        return bookId == objections.bookId && reservationId == objections.reservationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, reservationId);
    }
}