package com.library.repository;

import io.micrometer.common.lang.NonNull;
import com.library.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторій для роботи з таблицею статусів книг (book_status) у MySQL.
 */
@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Integer> {

    /**
     * Перевизначений стандартний метод отримання всіх статусів.
     * Аннотація @NonNull гарантує, що метод ніколи не поверне null (замість цього буде порожній список),
     * що захищає програму від NullPointerException.
     */
    @NonNull
    List<BookStatus> findAll();
}