package com.library.controller;

import com.library.dto.CartDto;
import com.library.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контролер для керування операціями над кошиком.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartRestController {

    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Ендпоінт: GET /api/cart
     * Повертає список книг у кошику поточного авторизованого користувача.
     */
    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        CartDto cart = cartService.getOrCreateCart(email);
        return ResponseEntity.ok(cart);
    }

    /**
     * DELETE /api/cart/books/{bookId}
     * Видаляє конкретну книгу з кошика поточного користувача.
     */
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<String> removeBookFromCart(@PathVariable int bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        cartService.removeBookFromCart(email, bookId);
        return ResponseEntity.ok("Книгу успішно видалено з кошика.");
    }

    /**
     * POST /api/cart/books/{bookId}
     * Додає обрану книгу до персонального кошика поточного користувача.
     * Викликається кнопкою "Додати в кошик" на інтерфейсі каталогу.
     */
    @PostMapping("/books/{bookId}")
    public ResponseEntity<String> addBookToCart(@PathVariable int bookId) {
        // Витягуємо email користувача з контексту безпеки JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Викликаємо бізнес-логіку додавання
        cartService.addBookToCart(email, bookId);

        return ResponseEntity.ok("Книгу успішно додано до вашого кошика.");
    }
}