package com.library.repository;

import com.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для роботи з таблицею жанрів (genre) у MySQL.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
