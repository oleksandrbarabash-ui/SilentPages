package com.library.service;

import com.library.model.Book;
import com.library.model.Genre;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.repository.GenreRepository;
import com.library.repository.BookStatusRepository;
import com.library.dto.BookDto;
import com.library.repository.specification.BookSpecifications;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookStatusRepository bookStatusRepository;
    private final GenreRepository genreRepository;

    // Рекомендуемый способ внедрения зависимостей (через конструктор)
    public BookService(BookRepository bookRepository,
                       BookStatusRepository bookStatusRepository,
                       GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.genreRepository = genreRepository;
    }

    // Главный метод: динамическая фильтрация, поиск и пагинация
    public Page<BookDto> getBooks(String search, Integer genreId, Integer statusId, Pageable pageable) {

        Specification<Book> spec = Specification.where(BookSpecifications.fetchGenreAndStatus())
                .and(BookSpecifications.hasSearchText(search))
                .and(BookSpecifications.hasGenreId(genreId))
                .and(BookSpecifications.hasStatusId(statusId));

        Page<Book> booksPage = bookRepository.findAll(spec, pageable);

        return booksPage.map(book -> new BookDto(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getLanguage(),
                book.getPages(),
                book.getGenre() != null ? book.getGenre().getName() : "Без жанру",
                book.getBookStatus() != null ? book.getBookStatus().getName() : "Без статусу"
        ));
    }

    // Получение отдельной книги по ID (пригодится для следующих задач)
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Сохранение книги
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    // Обновление книги
    public void updateBook(Book book, int genreId, int statusId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Жанр не знайдено"));

        BookStatus status = bookStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Статус не знайдено"));

        book.setGenre(genre);
        book.setBookStatus(status);
        bookRepository.save(book);
    }

    // Получение списка всех жанров для эндпоинта GET /api/genres
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
