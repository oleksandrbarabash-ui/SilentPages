package com.library.service;

import com.library.model.Book;
import com.library.model.Genre;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.repository.GenreRepository;
import com.library.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.library.dto.BookDto;
import java.util.List;


@Service
public class BookService {

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Autowired
    private GenreRepository genreRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAllwGenreStatus();
    }

    public List<Book> getBooksByGenre(int genreId) {
        return bookRepository.findByGenreId(genreId);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByNameContaining(keyword);
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(Book book, int genreId, int statusId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Жанр не знайдено"));

        BookStatus status = bookStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Статус не знайдено"));

        book.setGenre(genre);
        book.setBookStatus(status);
        bookRepository.save(book);
    }

    public Page<BookDto> getBooksWithPagination(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllWithGenreAndStatus(pageable);

        // Преобразуем страницу Entity в страницу DTO
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

    public Page<BookDto> getBooks(String search, Pageable pageable) {
        Page<Book> booksPage;

        // Если поисковый запрос передан и он не пустой — ищем по нему
        if (search != null && !search.trim().isEmpty()) {
            booksPage = bookRepository.searchByNameOrAuthor(search, pageable);
        } else {
            // Иначе просто отдаем все книги с пагинацией (наш прошлый метод)
            booksPage = bookRepository.findAllWithGenreAndStatus(pageable);
        }

        // Трансформируем в DTO
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



}

