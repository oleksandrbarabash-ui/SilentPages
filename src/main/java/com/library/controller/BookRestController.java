package com.library.controller;

import com.library.dto.BookDto;
import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

/**
 * REST-контролер для керування ресурсами книг.
 * Призначений для обробки HTTP-запитів, пов'язаних із каталогом книг,
 * та повернення відповідей у форматі JSON.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Дозволяє запити з будь-яких доменів (необхідно для інтеграції з фронтендом)
public class BookRestController {

    private final BookService bookService;

    /**
     * Конструктор контролера.
     * Використовується для впровадження залежності сервісу (Dependency Injection) через конструктор,
     * що є стандартом безпеки та полегшує тестування.
     */
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Ендпоінт: GET /api/books
     * Повертає сторінку із списком книг з урахуванням фільтрів та пагінації.
     * Забезпечує фронтенд даними для динамічного відображення каталогу бібліотеки.
     * * @param search   Необов'язковий текст для пошуку за назвою або автором.
     * @param genreId  Необов'язковий ID жанру для фільтрації.
     * @param statusId Необов'язковий ID статусу книги для фільтрації.
     * @param pageable Параметри пагинації (номер сторінки та розмір), за замовчуванням сторінка 0, розмір 10.
     * @return Об'єкт ResponseEntity, що містить сторінку з BookDto та HTTP-статус 200 (OK).
     */
    @GetMapping("/books")
    public ResponseEntity<Page<BookDto>> getAllBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String language,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {


        Page<BookDto> books = bookService.getBooks(search, genreId, statusId, language, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Ендпоінт: GET /api/books/{id}
     * Знаходить одну книгу за її ID.
     * Використовується фронтендом для відкриття сторінки (картки) конкретної книги.
     * @param id ID книги, який передається в URL (наприклад, /api/books/5).
     * @return JSON з даними книги (200 OK) або пуста відповідь з кодом 404 Not Found.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable int id) {
        BookDto bookDto = bookService.getBookDtoById(id);

        if (bookDto == null) {
            // Замість порожнього .notFound().build() викидаємо помилку, яку красиво обробить нашExceptionHandler
            throw new IllegalArgumentException("Книгу з ID " + id + " не знайдено в базі даних.");
        }

        return ResponseEntity.ok(bookDto);
    }
    /**
     * Ендпоінт: DELETE /api/books/{id}
     * Імітує видалення книги. Доступний ТІЛЬКИ для користувачів з роллю 'admin'.
     * Для перевірки обмеження доступу на основі ролей (RBAC).
     */
    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Spring автоматично шукатиме "ROLE_ADMIN" в контексті
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        return ResponseEntity.ok("Успіх: Книгу з ID " + id + " видалено адміністратором.");
    }

    /**
     * Ендпоінт: POST /api/books
     * Що робить: Приймає дані нової книги у форматі JSON, а також ID жанру та статусу.
     * Доступний ТІЛЬКИ для адміністраторів додатка.
     * Навіщо потрібен: Дозволяє безпечно додавати нові видання через адмін-панель з валідацією на дублікати.
     */
    @PostMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createBook(
            @RequestBody Book book,
            @RequestParam int genreId,
            @RequestParam int statusId) {

        bookService.createBook(book, genreId, statusId);
        return ResponseEntity.status(201).body("Книгу успішно додано до бази даних.");
    }

    /**
     * GET /api/books/languages
     * Повертає масив унікальних мов, які зараз є в бібліотеці.
     */
    @GetMapping("/books/languages")
    public ResponseEntity<List<String>> getAvailableLanguages() {
        return ResponseEntity.ok(bookService.getAllLanguages());
    }
}