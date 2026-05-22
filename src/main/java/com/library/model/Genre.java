package com.library.model;

import jakarta.persistence.*;

/**
 * Доменна сутність (Entity) для таблиці "genre".
 * Описує довідник літературних жанрів книг
 */
@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Первинний ключ жанру

    private String name; // Назва жанру

    /**
     * Порожній конструктор для потреб JPA (Hibernate).
     */
    public Genre() {}

    /**
     * Конструктор для створення жанру лише за назвою (id згенерується базою даних).
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Конструктор для створення об'єкта із чітко вказаним ID (наприклад, для тестів або мапінгу).
     */
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Геттери та Сеттери для доступу до полів жанру.
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}