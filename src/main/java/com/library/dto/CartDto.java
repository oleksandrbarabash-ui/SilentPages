package com.library.dto;

import java.util.List;

/**
 * DTO для повернення вмісту кошика клієнту.
 * Передає ідентифікатор кошика та список книг, адаптованих для фронтенду.
 */
public class CartDto {
    private int id;
    private List<BookDto> books;

    public CartDto(int id, List<BookDto> books) {
        this.id = id;
        this.books = books;
    }

    public int getId() { return id; }
    public List<BookDto> getBooks() { return books; }
}