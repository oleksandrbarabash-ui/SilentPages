package com.library.controller;

import com.library.dto.AdminReservationDto;
import com.library.dto.ReservationDto;
import com.library.dto.StatusUpdateRequest;
import com.library.service.ReservationService;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * REST-контролер для службових операцій бібліотекаря/адміністратора над бронюваннями.
 * Доступ мають виключно користувачі з роллю ADMIN.
 */
@RestController
@RequestMapping("/api/admin/reservations")
@PreAuthorize("hasRole('ADMIN')") // Бар'єр безпеки на всі методи класу
@CrossOrigin(origins = "*")
public class AdminReservationRestController {

    private final ReservationService reservationService;

    public AdminReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * GET /api/admin/reservations
     * Повертає повний список замовлень усіх користувачів системи.
     */
    @GetMapping
    public ResponseEntity<List<AdminReservationDto>> getAllReservations() {
        List<AdminReservationDto> reservations = reservationService.getAllReservationsForAdmin();
        return ResponseEntity.ok(reservations);
    }

    /**
     * PATCH /api/admin/reservations/{id}/status
     * Оновлює статус обраного замовлення (наприклад, переводить з "Очікування" в "Підтверджено").
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody StatusUpdateRequest request) {
        reservationService.updateReservationStatus(id, request);
        return ResponseEntity.ok("Статус бронювання №" + id + " успішно оновлено бібліотекарем.");
    }

    /**
     * GET /api/admin/reservations/user/{userId}
     * Повертає посторінковий список бронювань конкретного користувача.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationDto>> getUserReservations(
            @PathVariable int userId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<ReservationDto> userReservations = reservationService.getUserReservationsForAdmin(userId, pageable);
        return ResponseEntity.ok(userReservations);
    }
}