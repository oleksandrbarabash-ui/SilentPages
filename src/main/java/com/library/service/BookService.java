package com.library.service;

import com.library.model.Book;
import com.library.model.Genre;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.repository.GenreRepository;
import com.library.repository.BookStatusRepository;
import com.library.dto.BookDto;
import com.library.repository.specification.BookSpecifications;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Сервісний компонент бізнес-логіки додатку "Бібліотека".
 * Центральний міст між Контролерами (веб-рівнем) та Репозиторіями (рівнем БД).
 * Контролює правила обробки даних, валідацію, мапінг у DTO та агрегує роботу з БД.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookStatusRepository bookStatusRepository;
    private final GenreRepository genreRepository;

    /**
     * Єдиний конструктор сервісу для впровадження всіх репозиторіїв.
     * (Прибрано дублювання полів та аннотації @Autowired, тепер ініціалізація чиста та безпечна).
     */
    public BookService(BookRepository bookRepository,
                       BookStatusRepository bookStatusRepository,
                       GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.genreRepository = genreRepository;
    }

    /**
     * Збирає динамічну специфікацію на основі вхідних фільтрів, робить запит до бази даних
     * та конвертує отриману сторінку сутностей Book у сторінку безпечних об'єктів BookDto.
     * Головне серце каталогу книг. Забезпечує одночасний пошук, пагінацію та фільтрацію.
     * * @return Сторінка Page, заповнена BookDto з метаданими для фронтенду.
     */
    public Page<BookDto> getBooks(String search, Integer genreId, Integer statusId, String language, Pageable pageable) {

        // Комбінуємо шматочки SQL-запиту в одне ціле, додаючи фільтр за мовою
        Specification<Book> spec = Specification.where(BookSpecifications.fetchGenreAndStatus())
                .and(BookSpecifications.hasSearchText(search))
                .and(BookSpecifications.hasGenreId(genreId))
                .and(BookSpecifications.hasStatusId(statusId))
                .and(BookSpecifications.hasLanguage(language));

        // Виконуємо оптимізований запит у MySQL
        Page<Book> booksPage = bookRepository.findAll(spec, pageable);

        // Трансформуємо у DTO
        return booksPage.map(book -> new BookDto(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getLanguage(),
                book.getPages(),
                book.getGenre() != null ? book.getGenre().getName() : "Без жанру",
                book.getBookStatus() != null ? book.getBookStatus().getName() : "Без статусу",
                book.getDescription() // <-- Додаємо виклик геттера опису
        ));
    }

    /**
     * Шукає конкретну книгу за її унікальним ідентифікатором (ID).
     * Потрібен для перегляду картки окремої книги або для перевірки її існування перед видаленням/редагуванням.
     */
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Що робить: Шукає книгу за ID та трансформує її у безпечний об'єкт BookDto.
     * Забезпечує роботу ендпоінту отримання детальної інформації про одну книгу.
     * @param id Унікальний ідентифікатор книги.
     * @return Об'єкт BookDto або null, якщо книгу не знайдено.
     */
    public BookDto getBookDtoById(int id) {
        // Дістаємо сутність Book з бази даних
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return null;
        }

        // Конвертуємо в DTO (точно так само, як у списку книг)
        return new BookDto(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getLanguage(),
                book.getPages(),
                book.getGenre() != null ? book.getGenre().getName() : "Без жанру",
                book.getBookStatus() != null ? book.getBookStatus().getName() : "Без статусу",
                book.getDescription()
        );
    }

    /**
     * Оновлює існуючу книгу.
     * Захищає цілісність бази даних, перевіряючи наявність книги, жанру та статусу.
     */
    @Transactional
    public void updateBook(int bookId, Book updatedBookData, int genreId, int statusId) {
        // 1. Знаходимо існуючу книгу
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книгу з ID " + bookId + " не знайдено."));

        // 2. Знаходимо жанр та статус
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Жанр не знайдено"));

        BookStatus status = bookStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Статус не знайдено"));

        // 3. Оновлюємо поля
        existingBook.setName(updatedBookData.getName());
        existingBook.setAuthor(updatedBookData.getAuthor());
        existingBook.setLanguage(updatedBookData.getLanguage());
        existingBook.setPages(updatedBookData.getPages());
        existingBook.setDescription(updatedBookData.getDescription());

        // 4. Оновлюємо зв'язки
        existingBook.setGenre(genre);
        existingBook.setBookStatus(status);

        // Зберігаємо оновлену книгу
        bookRepository.save(existingBook);
    }

    /**
     * Перевіряє книгу на унікальність за назвою, автором, мовою та сторінками.
     * Якщо копій немає — прив'язує жанр, статус і створює новий запис у БД.
     * Для безпечного додавання нових книг адміном без дублювання даних.
     */
    public void createBook(Book book, int genreId, int statusId) {

        boolean isDuplicate = bookRepository.existsByNameAndAuthorAndLanguageAndPages(
                book.getName(),
                book.getAuthor(),
                book.getLanguage(),
                book.getPages()
        );

        if (isDuplicate) {
            throw new IllegalArgumentException("Така книга ('" + book.getName() + "' від автора " + book.getAuthor() + ") вже існує в каталозі.");
        }

        // 2. Шукаємо жанр та статус, як і при оновленні
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Жанр не знайдено"));

        BookStatus status = bookStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Статус не знайдено"));

        book.setGenre(genre);
        book.setBookStatus(status);
        book.setDescription(book.getDescription());

        // 3. Зберігаємо
        bookRepository.save(book);
    }

    /**
     * Витягує всі наявні жанри з БД.
     * Передає дані репозиторію до відповідного REST-контролера для довідника фільтрів.
     */
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
    /**
     * Повертає список унікальних мов для фільтра на фронтенді.
     * Щоб у випадаючому списку не було мов, для яких немає книг.
     */
    public List<String> getAllLanguages() {
        return bookRepository.findDistinctLanguages();
    }
}