package com.library.service;

import com.library.model.ReservationBook;
import com.library.model.User;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender; // Вбудований інструмент Spring для пошти

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Надсилання реального листа: Попередження за 1 день.
     */
    public void sendWarningEmail(ReservationBook rb) {
        User user = rb.getReservation().getOwner();

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("❌ [Email Service] Скасовано: відсутній email.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kocmaria943@gmail.com");
            message.setTo(user.getEmail());
            message.setSubject("Нагадування: Термін повернення книги добігає кінця!");

            String text = "Шановний(а) " + user.getFirstname() + "!\n\n" +
                    "Нагадуємо, що завтра закінчується строк бронювання книги:\n" +
                    "• \"" + rb.getBook().getName() + "\" (Автор: " + rb.getBook().getAuthor() + ")\n\n" +
                    "Будь ласка, поверніть її до бібліотеки до " + rb.getEndDate() + " включно, щоб уникнути блокування подальших бронювань.\n\n" +
                    "З повагою,\nВаша Бібліотека.";

            message.setText(text);
            mailSender.send(message); // Відправляємо лист

            System.out.println("✉️ [Email Service] Успішно надіслано попередження на: " + user.getEmail());
        } catch (MailException e) {
            System.err.println("❌ [Email Service] Помилка надсилання листа на " + user.getEmail() + ": " + e.getMessage());
        }
    }

    /**
     * Надсилання реального листа: Прострочення.
     */
    public void sendOverdueEmail(ReservationBook rb) {
        User user = rb.getReservation().getOwner();

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("❌ [Email Service] Скасовано: відсутній email.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kocmaria943@gmail.com");
            message.setTo(user.getEmail());
            message.setSubject("Увага: Строк повернення книги ПРОСТРОЧЕНО!");

            String text = "Шановний(а) " + user.getFirstname() + "!\n\n" +
                    "Повідомляємо, що строк повернення книги офіційно МИНУВ:\n" +
                    "• \"" + rb.getBook().getName() + "\" (Автор: " + rb.getBook().getAuthor() + ")\n\n" +
                    "Дата офіційного закінчення була: " + rb.getEndDate() + "\n" +
                    "Ваш поточний статус у системі змінено на 'Прострочено'. Будь ласка, терміново поверніть примірник.\n\n" +
                    "З повагою,\nВаша Бібліотека.";

            message.setText(text);
            mailSender.send(message); // Відправляємо лист!

            System.out.println("🚨 [Email Service] Успішно надіслано сповіщення про прострочення на: " + user.getEmail());
        } catch (MailException e) {
            System.err.println("❌ [Email Service] Помилка надсилання листа на " + user.getEmail() + ": " + e.getMessage());
        }
    }
}