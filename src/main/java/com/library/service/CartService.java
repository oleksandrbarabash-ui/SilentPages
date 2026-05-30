package com.library.service;

import com.library.dto.BookDto;
import com.library.dto.CartDto;
import com.library.model.Book;
import com.library.model.ShoppingCart;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.ShoppingCartRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервіс для керування логікою персональних кошиків користувачів.
 */
@Service
public class CartService {

    private final ShoppingCartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartService(ShoppingCartRepository cartRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Знаходить або створює кошик поточного користувача за його email,
     * після чого мапить список знайдених книг у формат BookDto.
     * Гарантує, що користувач отримає доступ виключно до свого кошика.
     */
    @Transactional
    public CartDto getOrCreateCart(String email) {
        ShoppingCart cart = cartRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    // Якщо кошика ще немає в базі (наприклад, новий юзер), створюємо його автоматично
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено"));
                    return cartRepository.save(new ShoppingCart(user));
                });

        // Конвертуємо книги кошика в DTO
        List<BookDto> bookDtos = cart.getBooks().stream().map(book -> new BookDto(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getLanguage(),
                book.getPages(),
                book.getGenre() != null ? book.getGenre().getName() : "Без жанру",
                book.getBookStatus() != null ? book.getBookStatus().getName() : "Без статусу"
        )).collect(Collectors.toList());

        return new CartDto(cart.getId(), bookDtos);
    }

    /**
     * Видаляє обрану книгу зі списку книг у кошику поточного користувача.
     * Забезпечує виконання операції очищення кошика від зайвих видань перед бронюванням.
     */
    @Transactional
    public void removeBookFromCart(String email, int bookId) {
        ShoppingCart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Кошик для даного користувача не існує"));

        // Шукаємо книгу в кошику за її ID
        Book bookToRemove = cart.getBooks().stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Цієї книги немає у вашому кошику"));

        // Видаляємо книгу зі списку
        cart.getBooks().remove(bookToRemove);
        cartRepository.save(cart);
    }
}