package com.library.controller;

import com.library.model.Genre;
import com.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для роботи з жанрами.
 * Обробляє HTTP-запити, що стосуються довідника жанрів бібліотеки.
 */
@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
public class GenreRestController {

    private final BookService bookService;

    /**
     * Конструктор для впровадження бізнес-логіки (BookService) в контролер жанрів.
     */
    public GenreRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Ендпоінт: GET /api/genres
     * Запитує у сервісу та повертає повний список жанрів у форматі JSON.
     * Необхідний фронтенду, щоб заповнити випадаючі списки (Dropdown) фільтрації на інтерфейсі користувача.
     */
    @GetMapping
    public ResponseEntity<List<Genre>> getGenres() {
        return ResponseEntity.ok(bookService.getAllGenres());
    }
}
