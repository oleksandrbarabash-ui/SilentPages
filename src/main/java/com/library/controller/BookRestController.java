package com.library.controller;

import com.library.dto.BookDto;
import com.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Разрешает запросы с любых портов и доменов
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    // Эндпоинт GET /api/books
    @GetMapping("/books")
    public ResponseEntity<Page<BookDto>> getAllBooks(
            // @PageableDefault задает параметры по умолчанию: 0 страница, 10 книг на странице
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<BookDto> books = bookService.getBooksWithPagination(pageable);
        return ResponseEntity.ok(books);
    }
}