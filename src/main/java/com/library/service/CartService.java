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
                book.getBookStatus() != null ? book.getBookStatus().getName() : "Без статусу",
                book.getDescription()
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

    /**
     * Додає обрану книгу до кошика поточного користувача.
     * Перед додаванням перевіряє, чи існує книга, чи вона доступна для бронювання
     * та чи не була вона вже додана в кошик цього користувача.
     * Забезпечує бізнес-правила збору книг читачем перед бронюванням.
     */
    @Transactional
    public void addBookToCart(String email, int bookId) {
        // 1. Знаходимо або автоматично створюємо кошик користувача за його email
        ShoppingCart cart = cartRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено"));
                    return cartRepository.save(new ShoppingCart(user));
                });

        // 2. Критерій: Перевіряємо, чи існує книга в базі даних
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книгу з вказаним ID не знайдено в базі даних."));

        // 3. Обмеження: Перевіряємо статус доступності книги (якщо статус з ID = 2 "Немає в наявності")
        if (book.getBookStatus() != null && book.getBookStatus().getId() == 2) {
            throw new IllegalArgumentException("Ця книга наразі недоступна для бронювання.");
        }

        // 4. Обмеження: Перевіряємо, чи книга вже є в кошику, щоб уникнути дублювання
        boolean alreadyInCart = cart.getBooks().stream()
                .anyMatch(b -> b.getId() == bookId);

        if (alreadyInCart) {
            throw new IllegalArgumentException("Ця книга вже додана до вашого кошика.");
        }

        // 5. Якщо всі перевірки пройдено — додаємо книгу до списку кошика
        cart.getBooks().add(book);
        cartRepository.save(cart);
    }

}