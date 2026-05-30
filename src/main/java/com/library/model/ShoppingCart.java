package com.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Доменна сутність (Entity) для таблиці кошика "shopping_cart".
 * Відображає персональний кошик користувача, де зберігаються
 * обрані для подальшого бронювання книги.
 */
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Унікальний зв'язок "один до одного" з користувачем
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // Зв'язок "багато до багатьох" з книгами через проміжну таблицю cart_book
    @ManyToMany
    @JoinTable(
            name = "cart_book",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();

    public ShoppingCart() {}

    public ShoppingCart(User user) {
        this.user = user;
    }

    // Геттери та сеттери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}