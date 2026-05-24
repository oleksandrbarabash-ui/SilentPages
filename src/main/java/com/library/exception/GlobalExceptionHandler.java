package com.library.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Централізований (глобальний) обробник виключних ситуацій додатку.
 * Компонент "перехоплювач" помилок (Global Error Handling Middleware).
 * Служить захисним бар'єром бекенду. Перехоплює будь-які збої в контролерах,
 * логує їх для розробника та повертає об'єкт ErrorResponse у форматі JSON фронтенду,
 * не дозволяючи серверу повністю "впасти".
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Ініціалізація логера SLF4J для запису технічних збоїв у консоль додатка
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Перехоплювач загальних критичних помилок (код 500 Internal Server Error).
     * Обробляє непередбачені збої, такі як NullPointerException або краш бази даних.
     * Логує технічні деталі через logger.error та маскує їх для клієнта
     * безпечним загальним повідомленням, щоб не розкривати внутрішній устрій системи хакерам.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {

        // Запис критичної помилки в консоль з повним трасуванням стеку (stacktrace)
        logger.error("Критична помилка сервера: {}", ex.getMessage(), ex);

        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Виникла внутрішня помилка сервера. Спробуйте пізніше.",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Перехоплювач помилок некоректних запитів (код 400 Bad Request).
     * Обробляє помилки валідації або бізнес-логіки (IllegalArgumentException).
     * Логує проблему як попередження (WARN), адже сервер працює справно,
     * а помилка сталася через невалідні дані від фронтенду. Повертає клієнту точну причину відмови.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Помилка обробки запиту: {}", ex.getMessage());

        // 1. Якщо це помилка відсутності книги — 404 Not Found
        if (ex.getMessage().contains("не знайдено в базі даних")) {
            ErrorResponse errorDetails = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(), // 404
                    ex.getMessage(),
                    request.getDescription(false)
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        // 2. Якщо це помилка входу — 401 Unauthorized
        if (ex.getMessage().contains("Невірний email або пароль")) {
            ErrorResponse errorDetails = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(), // 401
                    ex.getMessage(),
                    request.getDescription(false)
            );
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        }

        // 3. Для всіх інших випадків  400 Bad Request
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), // 400
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Обробка поилки невірного HTTP-метода
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            org.springframework.web.HttpRequestMethodNotSupportedException ex, WebRequest request) {

        logger.warn("Спроба викликати ендпоінт непідтримуваним методом: {}", ex.getMethod());

        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Цей HTTP-метод не підтримується для даного ендпоінту. Перевірте тип запиту (POST/GET).",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Обробка помилки відсутності прав доступу (код 403 Forbidden)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {

        logger.warn("Спроба несанкціонованого доступу: {}", ex.getMessage());

        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(), // 403
                "Помилка доступу: У вас немає прав для виконання цієї операції (необхідна роль Administrator).",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
}