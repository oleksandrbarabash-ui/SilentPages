package com.library.dto;

/**
 * DTO (Data Transfer Object) для сутності "Книга".
 * Плаский Java-клас без логіки, який містить лише необхідні для клієнта поля.
 * Навіщо потрібен:
 * 1. Захищає структуру бази даних від прямого витоку на фронтенд.
 * 2. Спрощує об'єкт: замість вкладених об'єктів Genre та BookStatus передає лише їхні текстові назви.
 * 3. Запобігає помилкам циклічних посилань під час конвертації об'єктів Hibernate в JSON.
 */
public class BookDto {
    private int id;
    private String name;
    private String author;
    private String language;
    private int pages;
    private String genreName;  // Текстова назва жанру замість повного об'єкта
    private String statusName; // Текстова назва статусу замість повного об'єкта
    private String description;
    private int totalCopies;
    private int availableCopies;
    /**
     * Конструктор DTO.
     * Використовується сервісом для швидкого перенесення даних із сутності (Entity) в об'єкт передачі.
     */
    public BookDto(int id, String name, String author, String language, int pages,
                   String genreName, String statusName, String description,
                   int totalCopies, int availableCopies) { // <-- Оновлено
        this.id = id;
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.genreName = genreName;
        this.statusName = statusName;
        this.description = description;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    /**
     * Геттери полів.
     * Обов'язкові для бібліотеки Jackson (вбудованої в Spring),
     * яка автоматично конвертує цей Java-клас у текстовий формат JSON.
     */
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getLanguage() { return language; }
    public int getPages() { return pages; }
    public String getGenreName() { return genreName; }
    public String getStatusName() { return statusName; }
    public String getDescription() { return description; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
}