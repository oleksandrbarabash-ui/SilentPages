package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import com.library.dto.ReservationBookItemDto;
import com.library.dto.ReservationDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ShoppingCartRepository cartRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationBookRepository reservationBookRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationBookStatusRepository reservationBookStatusRepository;

    public ReservationService(ShoppingCartRepository cartRepository,
                              ReservationRepository reservationRepository,
                              ReservationBookRepository reservationBookRepository,
                              ReservationStatusRepository reservationStatusRepository,
                              ReservationBookStatusRepository reservationBookStatusRepository) {
        this.cartRepository = cartRepository;
        this.reservationRepository = reservationRepository;
        this.reservationBookRepository = reservationBookRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationBookStatusRepository = reservationBookStatusRepository;
    }

    /**
     * Створює загальне бронювання на основі книг із кошика,
     * реєструє кожну книгу в таблиці рядочків та повністю очищує кошик.
     * Реалізує головне бізнес-правило оформлення замовлення читачем.
     */
    @Transactional
    public void createReservationFromCart(String email) {
        // 1. Отримуємо кошик користувача
        ShoppingCart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Кошик користувача не знайдено."));

        // 2. Перевірка на порожній кошик
        if (cart.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Неможливо оформити бронювання: ваш кошик порожній.");
        }

        // 3. Завантажуємо системні початкові статуси з бази даних
        ReservationStatus initialStatus = reservationStatusRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Системний статус бронювання 'Очікування' не знайдено в БД."));

        ReservationBookStatus initialBookStatus = reservationBookStatusRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Системний статус книги в бронюванні 'Очікування' не знайдено в БД."));

        // 4. Створюємо «шапку» бронювання
        Reservation reservation = new Reservation();
        reservation.setOwner(cart.getUser());
        reservation.setStatus(initialStatus);
        reservation.setCreateTime(LocalDate.now());
        reservation.setStartDate(LocalDate.now());
        reservation.setUpdateTime(LocalDate.now());

        // Зберігаємо в базу, щоб згенерувався унікальний ID замовлення
        reservation = reservationRepository.save(reservation);

        // 5. Переносимо кожну книгу з кошика в рядки бронювання
        for (Book book : cart.getBooks()) {
            ReservationBook reservationBook = new ReservationBook(reservation, book, initialBookStatus);
            reservationBookRepository.save(reservationBook);
        }

        // 6. Критерій: Після успішного створення очищуємо список книг кошика
        cart.getBooks().clear();
        cartRepository.save(cart);
    }

    /**
     * Витягує всі бронювання користувача за його email, підтягує пов'язані
     * з ними книги та статуси, формуючи безпечний список DTO.
     * Для сторінки "Мої бронювання", повністю ізолює дані інших користувачів.
     */
    @Transactional(readOnly = true) //вказуємо, що тут ми тільки читаємо дані
    public List<ReservationDto> getMyReservations(String email) {
        // 1. Отримуємо всі бронювання поточного користувача
        List<Reservation> reservations = reservationRepository.findByOwnerEmailOrderByCreateTimeDesc(email);

        // 2. Проходимося по кожному бронюванню та перетворюємо його на DTO
        return reservations.stream().map(reservation -> {

            // 3. Для кожного бронювання дістаємо список його книг
            List<ReservationBook> reservationBooks = reservationBookRepository.findByReservationId(reservation.getId());

            // 4. Перетворюємо сутності ReservationBook на акуратні ReservationBookItemDto
            List<ReservationBookItemDto> bookDtos = reservationBooks.stream().map(rb -> new ReservationBookItemDto(
                    rb.getBook().getId(),
                    rb.getBook().getName(),
                    rb.getBook().getAuthor(),
                    rb.getStatus().getName()
            )).collect(Collectors.toList());

            // 5. Повертаємо зібране бронювання
            return new ReservationDto(
                    reservation.getId(),
                    reservation.getCreateTime(),
                    reservation.getStatus().getName(),
                    bookDtos
            );
        }).collect(Collectors.toList());
    }

}