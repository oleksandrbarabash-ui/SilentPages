package com.library.dto;

public class BookDto {
    private int id;
    private String name;
    private String author;
    private String language;
    private int pages;
    private String genreName;       // Вместо всего объекта Genre берем только название
    private String statusName;      // Вместо всего объекта BookStatus

    // Конструктор для удобной конвертации
    public BookDto(int id, String name, String author, String language, int pages, String genreName, String statusName) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.genreName = genreName;
        this.statusName = statusName;
    }

    // Геттеры и Сеттеры (Обязательно!)
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getLanguage() { return language; }
    public int getPages() { return pages; }
    public String getGenreName() { return genreName; }
    public String getStatusName() { return statusName; }
}
