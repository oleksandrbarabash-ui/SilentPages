package com.library.exception;

import java.time.LocalDateTime;

/**
 * Клас-контейнер (шаблон) для структуризації відповідей про помилки.
 * Стандартний об'єкт (POJO), який конвертується в JSON.
 * Замість передачі сирого системного тексту (stacktrace) клієнту,
 * цей клас дозволяє віддати фронтенду акуратну та зрозумілу структуру помилки.
 */
public class ErrorResponse {
    private int status;            // HTTP статус-код помилки (наприклад, 400, 404, 500)
    private LocalDateTime timestamp; // Точний час, коли стався збій на сервері
    private String message;         // Зрозуміле людині повідомлення про проблему
    private String details;         // Додаткові деталі (наприклад, URL-адреса запиту)

    /**
     * Конструктор для ініціалізації об'єкта помилки.
     * Автоматично фіксує поточний час створення помилки через LocalDateTime.now().
     */
    public ErrorResponse(int status, String message, String details) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    /**
     * Геттери полів.
     */
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
}