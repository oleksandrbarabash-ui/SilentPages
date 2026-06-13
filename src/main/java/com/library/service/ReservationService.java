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
import com.library.dto.AdminReservationDto;
import com.library.dto.StatusUpdateRequest;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ReservationService {

    private final ShoppingCartRepository cartRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationBookRepository reservationBookRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationBookStatusRepository reservationBookStatusRepository;
    private final BookStatusRepository bookStatusRepository;

    public ReservationService(ShoppingCartRepository cartRepository,
                              ReservationRepository reservationRepository,
                              ReservationBookRepository reservationBookRepository,
                              ReservationStatusRepository reservationStatusRepository,
                              ReservationBookStatusRepository reservationBookStatusRepository,
                              BookStatusRepository bookStatusRepository) { // <-- ДОДАНО ПАРАМЕТР
        this.cartRepository = cartRepository;
        this.reservationRepository = reservationRepository;
        this.reservationBookRepository = reservationBookRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationBookStatusRepository = reservationBookStatusRepository;
        this.bookStatusRepository = bookStatusRepository; // <-- ПРИСВОЄНО ЗНАЧЕННЯ
    }

    /**
     * Створює загальне бронювання на основі книг із кошика,
     * реєструє кожну книгу в таблиці рядочків та повністю очищує кошик.
     * Реалізує головне бізнес-правило оформлення замовлення читачем із перевіркою лімітів.
     */
    @Transactional
    public void createReservationFromCart(String email) {
        // Отримуємо кошик користувача
        ShoppingCart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Кошик користувача не знайдено."));

        // Перевірка на порожній кошик
        if (cart.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Неможливо оформити бронювання: ваш кошик порожній.");
        }

        // Перевірка ліміту активних бронювань (Максимум 5)
        List<Integer> activeStatuses = Arrays.asList(1, 3, 4); // Підтверджено, Очікування, Прострочено
        int activeReservationsCount = reservationRepository.countByOwnerEmailAndStatusIdIn(email, activeStatuses);

        if (activeReservationsCount >= 5) {
            throw new IllegalArgumentException("Перевищено ліміт: Ви не можете мати більше 5 активних бронювань одночасно.");
        }

        // Перевірка наявності кожної книги перед створенням бронювання
        for (Book book : cart.getBooks()) {
            if (book.getAvailableCopies() <= 0) {
                throw new IllegalArgumentException("Книга '" + book.getName() + "' закінчилася. Будь ласка, видаліть її з кошика перед оформленням.");
            }

            // Перевірка за ручним статусом книги (id = 2 -> "Немає в наявності")
            if (book.getBookStatus() != null && book.getBookStatus().getId() == 2) {
                throw new IllegalArgumentException("Книга '" + book.getName() + "' більше недоступна для бронювання. Будь ласка, видаліть її з кошика.");
            }
        }

        // 4. Завантажуємо системні початкові статуси з бази даних
        ReservationStatus initialStatus = reservationStatusRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Системний статус бронювання 'Очікування' не знайдено в БД."));

        ReservationBookStatus initialBookStatus = reservationBookStatusRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Системний статус книги в бронюванні 'Очікування' не знайдено в БД."));

        // 5. Створюємо «шапку» бронювання
        Reservation reservation = new Reservation();
        reservation.setOwner(cart.getUser());
        reservation.setStatus(initialStatus);
        reservation.setCreateTime(LocalDate.now());
        reservation.setStartDate(LocalDate.now());
        reservation.setUpdateTime(LocalDate.now());

        // Зберігаємо в базу, щоб згенерувався унікальний ID замовлення
        reservation = reservationRepository.save(reservation);

        // 6. Переносимо кожну книгу з кошика в рядки бронювання
        for (Book book : cart.getBooks()) {
            ReservationBook reservationBook = new ReservationBook(reservation, book, initialBookStatus);
            reservationBookRepository.save(reservationBook);
        }

        // 7. Критерій: Після успішного створення очищуємо список книг кошика
        cart.getBooks().clear();
        cartRepository.save(cart);
    }

    /**
     * Витягує всі бронювання користувача за його email, підтягує пов'язані
     * з ними книги та статуси, формуючи безпечний список DTO.
     * Для сторінки "Мої бронювання", повністю ізолює дані інших користувачів.
     */
    @Transactional(readOnly = true)
    public Page<ReservationDto> getMyReservations(String email, Pageable pageable) {

        // 1. Отримуємо сторінку сутностей із бази даних
        Page<Reservation> reservationsPage = reservationRepository.findByOwnerEmailOrderByCreateTimeDesc(email, pageable);

        // 2. Трансформуємо сторінку Entity у сторінку безпечних DTO
        return reservationsPage.map(reservation -> {

            // Для кожного бронювання дістаємо список його книг
            List<ReservationBook> reservationBooks = reservationBookRepository.findByReservationId(reservation.getId());

            // Перетворюємо сутності ReservationBook на акуратні ReservationBookItemDto
            List<ReservationBookItemDto> bookDtos = reservationBooks.stream().map(rb -> new ReservationBookItemDto(
                    rb.getBook().getId(),
                    rb.getBook().getName(),
                    rb.getBook().getAuthor(),
                    rb.getStatus().getName()
            )).collect(Collectors.toList());

            // Витягуємо дату закінчення з першої книги (якщо книги є)
            LocalDate reservationEndDate = reservationBooks.isEmpty() ? null : reservationBooks.get(0).getEndDate();

            // Повертаємо зібране бронювання
            return new ReservationDto(
                    reservation.getId(),
                    reservation.getCreateTime(),
                    reservation.getStatus().getName(),
                    reservationEndDate,
                    bookDtos
            );
        });
    }

    /**
     * Повертає список УСІХ бронювань у системі для панелі бібліотекаря.
     * Виконує критерій приймання щодо відображення повної службової сторінки.
     */
    @Transactional(readOnly = true)
    public List<AdminReservationDto> getAllReservationsForAdmin() {
        List<Reservation> reservations = reservationRepository.findAllByOrderByCreateTimeDesc();

        return reservations.stream().map(reservation -> {
            List<ReservationBook> reservationBooks = reservationBookRepository.findByReservationId(reservation.getId());

            List<ReservationBookItemDto> bookDtos = reservationBooks.stream().map(rb -> new ReservationBookItemDto(
                    rb.getBook().getId(),
                    rb.getBook().getName(),
                    rb.getBook().getAuthor(),
                    rb.getStatus().getName()
            )).collect(Collectors.toList());

            String fullName = reservation.getOwner().getFirstname() + " " + (reservation.getOwner().getLastname() != null ? reservation.getOwner().getLastname() : "");

            // Витягуємо дату закінчення з першої книги
            LocalDate reservationEndDate = reservationBooks.isEmpty() ? null : reservationBooks.get(0).getEndDate();

            return new AdminReservationDto(
                    reservation.getId(),
                    reservation.getOwner().getEmail(),
                    fullName.trim(),
                    reservation.getCreateTime(),
                    reservation.getUpdateTime(),
                    reservation.getStatus().getName(),
                    reservationEndDate,
                    bookDtos
            );
        }).collect(Collectors.toList());
    }

    /**
     * Змінює загальний статус бронювання на основі перевірки існуючих статусів в БД
     * та автоматично оновлює дату модифікації (update_time).
     * Дозволяє бібліотекарю підтверджувати або відхиляти заявки користувачів.
     */
    @Transactional
    public void updateReservationStatus(int reservationId, StatusUpdateRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Бронювання з ID " + reservationId + " не знайдено."));

        ReservationStatus newStatus = reservationStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Вказано некоректний статус."));

        // Якщо адміністратор підтверджує бронювання (id = 1), а воно ще не було підтверджене
        if (request.getStatusId() == 1 && reservation.getStatus().getId() != 1) {

            // Завантажуємо всі книги цього бронювання
            List<ReservationBook> booksInReservation = reservationBookRepository.findByReservationId(reservationId);

            // Перевірка доступності кожної книги
            for (ReservationBook rb : booksInReservation) {
                if (rb.getBook().getAvailableCopies() <= 0) {
                    throw new IllegalArgumentException("Неможливо підтвердити: книга '" + rb.getBook().getName() + "' відсутня на складі (доступно 0).");
                }
            }

            // Списання примірників зі складу
            for (ReservationBook rb : booksInReservation) {
                Book book = rb.getBook();
                book.setAvailableCopies(book.getAvailableCopies() - 1);

                // помилка під час збереження скасує попередні списання
                // і жодна книга не буде списана, якщо чогось не вистачає.
                rb.setEndDate(LocalDate.now().plusDays(14));
                // Якщо після списання доступних примірників стало 0
                if (book.getAvailableCopies() == 0) {
                    // Знаходимо статус "Немає в наявності"
                    BookStatus outOfStockStatus = bookStatusRepository.findById(2)
                            .orElseThrow(() -> new RuntimeException("Системний статус 'Немає в наявності' не знайдено"));

                    // Автоматично переводимо книгу в цей статус
                    book.setBookStatus(outOfStockStatus);
                }
            }



        }

        // Оновлюємо статус самого замовлення
        reservation.setStatus(newStatus);
        reservation.setUpdateTime(LocalDate.now());

        reservationRepository.save(reservation);
    }

    /**
     * Скасовує бронювання користувачем до його підтвердження адміністратором.
     * Забезпечує перевірку власника та поточного статусу (дозволено тільки для "Очікування").
     */
    @Transactional
    public void cancelReservationByClient(int reservationId, String email) {
        // 1. Шукаємо бронювання
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Бронювання з ID " + reservationId + " не знайдено."));

        // 2. Обмеження: Перевіряємо, чи це бронювання належить поточному авторизованому користувачу
        if (!reservation.getOwner().getEmail().equals(email)) {
            throw new IllegalArgumentException("Помилка доступу: Ви не можете скасувати чуже бронювання.");
        }

        // 3. Обмеження: Скасувати можна ТІЛЬКИ якщо статус "Очікування" (id = 3)
        if (reservation.getStatus().getId() != 3) {
            throw new IllegalArgumentException("Неможливо скасувати бронювання. Воно вже підтверджене, завершене або скасоване.");
        }

        // 4. Шукаємо статус "Скасовано" (id = 6)
        ReservationStatus cancelStatus = reservationStatusRepository.findById(6)
                .orElseThrow(() -> new RuntimeException("Системний статус 'Скасовано' не знайдено в БД."));

        // 5. Оновлюємо статус та фіксуємо час зміни
        reservation.setStatus(cancelStatus);
        reservation.setUpdateTime(LocalDate.now());

        // Зберігаємо зміни
        reservationRepository.save(reservation);
    }

    /**
     * Витягує бронювання конкретного клієнта за його ID (для Адміністратора).
     */
    @Transactional(readOnly = true)
    public Page<ReservationDto> getUserReservationsForAdmin(int userId, Pageable pageable) {
        // Знаходимо бронювання конкретного клієнта
        Page<Reservation> reservationsPage = reservationRepository.findByOwnerIdOrderByCreateTimeDesc(userId, pageable);

        // Конвертуємо у DTO
        return reservationsPage.map(reservation -> {
            List<ReservationBook> reservationBooks = reservationBookRepository.findByReservationId(reservation.getId());

            List<ReservationBookItemDto> bookDtos = reservationBooks.stream().map(rb -> new ReservationBookItemDto(
                    rb.getBook().getId(),
                    rb.getBook().getName(),
                    rb.getBook().getAuthor(),
                    rb.getStatus().getName()
            )).collect(Collectors.toList());

            LocalDate reservationEndDate = reservationBooks.isEmpty() ? null : reservationBooks.get(0).getEndDate();

            return new ReservationDto(
                    reservation.getId(),
                    reservation.getCreateTime(),
                    reservation.getStatus().getName(),
                    reservationEndDate,
                    bookDtos
            );
        });
    }

}