package com.library.controller;

import com.library.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.library.dto.ReservationDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * POST /api/reservations
     * Оформлює бронювання на основі поточного кошика авторизованого юзера.
     */
    @PostMapping
    public ResponseEntity<String> createReservation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        reservationService.createReservationFromCart(email);

        return ResponseEntity.status(201).body("Бронювання успішно оформлено! Ви можете переглянути його у вкладці 'Мої бронювання'.");
    }

    /**
     * GET /api/reservations/my
     * Повертає список бронювань поточного авторизованого користувача з підтримкою пагінації.
     */
    @GetMapping("/my")
    public ResponseEntity<Page<ReservationDto>> getMyReservations(
            @PageableDefault(page = 0, size = 10) Pageable pageable) { // <-- Додано Pageable

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Передаємо pageable у сервіс
        Page<ReservationDto> myReservations = reservationService.getMyReservations(email, pageable);

        return ResponseEntity.ok(myReservations);
    }

    /**
     * Ендпоінт: PATCH /api/reservations/{id}/cancel
     * Дозволяє читачу самостійно скасувати власне бронювання.
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelMyReservation(@PathVariable int id) {
        // Витягуємо email з токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Викликаємо сервіс
        reservationService.cancelReservationByClient(id, email);

        return ResponseEntity.ok("Ваше бронювання №" + id + " було успішно скасовано.");
    }
}