package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для сутності Book.
 * Надає доступ до таблиці книг у MySQL.
 * Спадкування від JpaRepository дає базові CRUD-операції (save, delete, findById).
 * Спадкування від JpaSpecificationExecutor додає підтримку динамічних фільтрів (Specifications).
 * (Тут видалено невикористані імпорти List та Specification, інтерфейс залишається чистим).
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    /**
     * Перевіряє, чи існує вже книга з ідентичними параметрами.
     */
    boolean existsByNameAndAuthorAndLanguageAndPages(String name, String author, String language, int pages);
    /**
     * Знаходить усі унікальні мови видання, які реально існують у каталозі книг.
     * Відкидає null-значення та сортує за алфавітом.
     */
    @Query("SELECT DISTINCT b.language FROM Book b WHERE b.language IS NOT NULL AND b.language != '' ORDER BY b.language ASC")
    List<String> findDistinctLanguages();
}
