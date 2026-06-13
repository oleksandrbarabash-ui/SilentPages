package com.library.repository;

import com.library.model.ReservationBook;
import com.library.model.ReservationBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface ReservationBookRepository extends JpaRepository<ReservationBook, ReservationBookId> {

    // Шукаємо всі рядки з книгами, які належать до конкретного бронювання
    List<ReservationBook> findByReservationId(int reservationId);

    // Знаходить усі книги зі вказаним статусом, дата закінчення яких уже минула
    List<ReservationBook> findByEndDateBeforeAndStatusId(LocalDate date, int statusId);
}