package com.library.repository;

import com.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Запрос с JOIN FETCH для жанра и статуса + поддержка пагинации
    @Query(value = "SELECT b FROM Book b LEFT JOIN FETCH b.genre LEFT JOIN FETCH b.bookStatus",
            countQuery = "SELECT count(b) FROM Book b")
    Page<Book> findAllWithGenreAndStatus(Pageable pageable);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.genre LEFT JOIN FETCH b.bookStatus")
    List<Book> findAllwGenreStatus();

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.genre g LEFT JOIN FETCH b.bookStatus WHERE g.id = :genreId")
    List<Book> findByGenreId(int genreId);

    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByNameContaining(String keyword);

}
