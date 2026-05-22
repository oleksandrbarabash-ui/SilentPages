package com.library.model;

import jakarta.persistence.*;

/**
 * Доменна сутність (Entity) для таблиці "book_status".
 * Описує можливі статуси книги в бібліотеці (наприклад: "В наявності", "Видана", "На реставрації").
 */
@Entity
@Table(name = "book_status")
public class BookStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Первинний ключ статусу

    private String name;        // Назва статусу
    private String description; // Додатковий опис, за що відповідає цей статус

    /**
     * Порожній конструктор для потреб JPA (Hibernate).
     */
    public BookStatus() {}

    /**
     * Конструктор для ініціалізації статусу базовими даними.
     */
    public BookStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Повний конструктор (використовується для повної ініціалізації об'єкта, включаючи опис).
     */
    public BookStatus(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Геттери та Сеттери для доступу та модифікації даних статусу.
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}