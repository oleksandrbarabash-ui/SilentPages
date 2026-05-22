package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для сутності Book.
 * Надає доступ до таблиці книг у MySQL.
 * Спадкування від JpaRepository дає базові CRUD-операції (save, delete, findById).
 * Спадкування від JpaSpecificationExecutor додає підтримку динамічних фільтрів (Specifications).
 * (Тут видалено невикористані імпорти List та Specification, інтерфейс залишається чистим).
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

}
