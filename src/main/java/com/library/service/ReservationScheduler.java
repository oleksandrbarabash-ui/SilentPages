package com.library.service;

import com.library.model.Reservation;
import com.library.model.ReservationBook;
import com.library.model.ReservationStatus;
import com.library.model.ReservationBookStatus;
import com.library.repository.ReservationRepository;
import com.library.repository.ReservationBookRepository;
import com.library.repository.ReservationStatusRepository;
import com.library.repository.ReservationBookStatusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервіс для автоматичних фонових перевірок (Cron Jobs).
 */
@Service
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationBookRepository reservationBookRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationBookStatusRepository reservationBookStatusRepository;

    public ReservationScheduler(ReservationRepository reservationRepository,
                                ReservationBookRepository reservationBookRepository,
                                ReservationStatusRepository reservationStatusRepository,
                                ReservationBookStatusRepository reservationBookStatusRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationBookRepository = reservationBookRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationBookStatusRepository = reservationBookStatusRepository;
    }

    /**
     * Автоматично перевіряє прострочені бронювання.
     * fixedRate = 60000 означає, що перевірка запускатиметься кожні 60 секунд (для тестів).
     * У реальному проекті тут буде cron = "0 0 0 * * ?" (раз на добу опівночі).
     */
    @Scheduled(fixedRate = 60000)
    @EventListener(ApplicationReadyEvent.class) // Запуск одразу після старту сервера
    @Transactional
    public void checkAndMarkOverdueReservations() {

        System.out.println("⏳ [Планувальник] Запуск перевірки прострочених бронювань...");

        LocalDate today = LocalDate.now();

        // 1. Шукаємо всі книги, які мають статус "Підтверджено" (id=1) і дата яких минула
        List<ReservationBook> overdueBooks = reservationBookRepository.findByEndDateBeforeAndStatusId(today, 1);

        if (overdueBooks.isEmpty()) {
            System.out.println("✅ [Планувальник] Прострочених книг не знайдено. Усі читачі молодці!");
            return;
        }

        // 2. Завантажуємо статуси "Прострочено" (id=4)
        ReservationStatus overdueResStatus = reservationStatusRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Системний статус 'Прострочено' не знайдено"));
        ReservationBookStatus overdueBookStatus = reservationBookStatusRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Системний статус книги 'Прострочено' не знайдено"));

        // 3. Змінюємо статуси
        for (ReservationBook rb : overdueBooks) {
            rb.setStatus(overdueBookStatus);

            Reservation reservation = rb.getReservation();
            if (reservation.getStatus().getId() != 4) {
                reservation.setStatus(overdueResStatus);
                reservation.setUpdateTime(LocalDate.now());
                reservationRepository.save(reservation);
            }
        }

        System.out.println("⚠️ [Планувальник] Увага! Знайдено та прострочено замовлень: " + overdueBooks.size());
    }
}