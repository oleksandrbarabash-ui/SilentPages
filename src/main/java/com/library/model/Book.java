package com.library.model;

import jakarta.persistence.*;

/**
 * Доменна сутність (Entity) для таблиці "book" у базі даних.
 * Клас, який повністю відображає (мапить) структуру таблиці книг MySQL на Java-об'єкт.
 * Необхідний для роботи ORM Hibernate, щоб автоматично виконувати SQL-запити
 * та зв'язувати книги з іншими таблицями за допомогою анотацій зв'язків.
 */
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Первинний ключ таблиці (автоінкремент)

    private String name;     // Назва книги
    private String author;   // Автор книги
    private String language; // Мова видання
    private int pages;       // Кількість сторінок
    // Зв'язок "Багато книг до одного статусу" (Foreign Key: book_status_id)
    @ManyToOne
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatus;

    // Зв'язок "Багато книг до одного жанру" (Foreign Key: genre_id)
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Конструктор за замовчуванням (Порожній).
     * Обов'язкова вимога специфікації JPA. Без нього Hibernate
     * не зможе створити об'єкт книги, коли буде діставати дані з бази.
     */
    public Book () {}

    /**
     * Конструктор з параметрами.
     * Використовується у коді розробника для швидкого створення
     * нового об'єкта книги перед збереженням її в базу даних.
     */
    public Book(String name, String author, String language, int pages, BookStatus bookStatus, Genre genre, String description) {
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.bookStatus = bookStatus;
        this.genre = genre;
        this.description = description;
    }

    /**
     * Стандартні Геттери та Сеттери.
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    public BookStatus getBookStatus() { return bookStatus; }
    public void setBookStatus(BookStatus bookStatus) { this.bookStatus = bookStatus; }
    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}