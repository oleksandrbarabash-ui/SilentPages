package com.library.repository;

import com.library.model.Reservation;
import com.library.model.ReservationBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Шукаємо всі бронювання юзера за його email, сортуючи від найновіших до найстаріших
    List<Reservation> findByOwnerEmailOrderByCreateTimeDesc(String email);

    // Витягуємо абсолютно всі бронювання для адмін-панелі
    List<Reservation> findAllByOrderByCreateTimeDesc();
    // Рахує кількість бронювань користувача, статуси яких входять у переданий список
    int countByOwnerEmailAndStatusIdIn(String email, List<Integer> activeStatusIds);

    // Додаємо підтримку Pageable для списку бронювань конкретного користувача
    Page<Reservation> findByOwnerEmailOrderByCreateTimeDesc(String email, Pageable pageable);

    // Пошук бронювань конкретного користувача за його ID (з пагінацією)
    Page<Reservation> findByOwnerIdOrderByCreateTimeDesc(int ownerId, Pageable pageable);


}