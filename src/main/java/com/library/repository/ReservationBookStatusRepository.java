package com.library.repository;

import com.library.model.ReservationBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationBookStatusRepository extends JpaRepository<ReservationBookStatus, Integer> {
}