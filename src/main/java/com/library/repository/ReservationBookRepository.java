package com.library.repository;

import com.library.model.ReservationBook;
import com.library.model.ReservationBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationBookRepository extends JpaRepository<ReservationBook, ReservationBookId> {
}