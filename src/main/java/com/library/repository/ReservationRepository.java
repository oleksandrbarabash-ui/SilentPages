package com.library.repository;

import com.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Шукаємо всі бронювання юзера за його email, сортуючи від найновіших до найстаріших
    List<Reservation> findByOwnerEmailOrderByCreateTimeDesc(String email);

    // Витягуємо абсолютно всі бронювання для адмін-панелі
    List<Reservation> findAllByOrderByCreateTimeDesc();
}