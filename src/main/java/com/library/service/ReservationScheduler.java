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

// Видалили імпорти EventListener, вони нам більше не потрібні

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationBookRepository reservationBookRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationBookStatusRepository reservationBookStatusRepository;
    private final EmailNotificationService emailNotificationService;

    public ReservationScheduler(ReservationRepository reservationRepository,
                                ReservationBookRepository reservationBookRepository,
                                ReservationStatusRepository reservationStatusRepository,
                                ReservationBookStatusRepository reservationBookStatusRepository,
                                EmailNotificationService emailNotificationService) {
        this.reservationRepository = reservationRepository;
        this.reservationBookRepository = reservationBookRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationBookStatusRepository = reservationBookStatusRepository;
        this.emailNotificationService = emailNotificationService;
    }

    /**
     * Фоновий планувальник.
     * initialDelay = 5000: Зачекати 5 секунд після старту сервера перед першим запуском.
     * fixedRate = 60000: Далі повторювати кожну хвилину.
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60000)
    @Transactional
    public void processLibraryAutomation() {
        System.out.println("⏳ [Планувальник] Запуск автоматичної перевірки статусів та сповіщень...");

        // 1. Міняємо статуси прострочень у базі
        checkAndMarkOverdueReservations();

        // 2. Обробляємо надсилання Email-повідомлень
        sendEmailNotifications();
    }

    /**
     * Логіка автоматичної зміни статусів у БД.
     */
    private void checkAndMarkOverdueReservations() {
        LocalDate today = LocalDate.now();
        List<ReservationBook> overdueBooks = reservationBookRepository.findByEndDateBeforeAndStatusId(today, 1);

        if (overdueBooks.isEmpty()) {
            System.out.println("✅ [Планувальник статусів] Прострочень для оновлення не знайдено.");
            return;
        }

        ReservationStatus overdueResStatus = reservationStatusRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Системний статус 'Прострочено' не знайдено"));
        ReservationBookStatus overdueBookStatus = reservationBookStatusRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Системний статус книги 'Прострочено' не знайдено"));

        for (ReservationBook rb : overdueBooks) {
            rb.setStatus(overdueBookStatus);
            Reservation reservation = rb.getReservation();
            if (reservation.getStatus().getId() != 4) {
                reservation.setStatus(overdueResStatus);
                reservation.setUpdateTime(LocalDate.now());
                reservationRepository.save(reservation);
            }
        }
        System.out.println("⚡ [Планувальник статусів] Статуси оновлено на 'Прострочено' для книг у кількості: " + overdueBooks.size());
    }

    /**
     * Логіка розсилки сповіщень (Завдання 1, 2, 3, 4).
     */
    private void sendEmailNotifications() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // --- ЧАСТИНА 1: Попередження за 1 день (Завдання 1) ---
        List<ReservationBook> warningBooks = reservationBookRepository
                .findByEndDateAndStatusIdAndEmailWarningSentFalse(tomorrow, 1);

        for (ReservationBook rb : warningBooks) {
            emailNotificationService.sendWarningEmail(rb);
            rb.setEmailWarningSent(true); // Захист від дублікатів (Завдання 4)
            reservationBookRepository.save(rb);
        }

        // --- ЧАСТИНА 2: Сповіщення про фактичне прострочення (Завдання 2) ---
        // Використовуємо статус 4 (Прострочено), оскільки перша частина методу їх уже перевела в цей статус
        List<ReservationBook> overdueBooksForEmail = reservationBookRepository
                .findByEndDateBeforeAndStatusIdAndEmailOverdueSentFalse(today, 4);

        for (ReservationBook rb : overdueBooksForEmail) {
            emailNotificationService.sendOverdueEmail(rb);
            rb.setEmailOverdueSent(true); // Захист від дублікатів (Завдання 4)
            reservationBookRepository.save(rb);
        }

        System.out.println("📨 [Планувальник Email] Перевірку розсилки завершено. Надіслано попереджень: "
                + warningBooks.size() + ", сповіщень про прострочення: " + overdueBooksForEmail.size());
    }
}