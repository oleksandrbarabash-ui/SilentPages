package com.library.controller;

import com.library.dto.ContactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контролер для отримання публічної контактної інформації бібліотеки.
 * Доступний усім користувачам (навіть неавторизованим гостям).
 */
@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactRestController {

    // Читаємо дані з application.properties.
    // Двокрапка (:) наприкінці означає значення за замовчуванням (порожній рядок), якщо поле видалять з файлу.
    @Value("${library.contact.email:}")
    private String email;

    @Value("${library.contact.phone:}")
    private String phone;

    @Value("${library.contact.address:}")
    private String address;

    @Value("${library.contact.working-hours:}")
    private String workingHours;

    /**
     * GET /api/contacts
     * Повертає актуальні контактні дані.
     * Якщо всі контакти відсутні, повертає статус 204 No Content, щоб фронтенд міг приховати блок.
     */
    @GetMapping
    public ResponseEntity<ContactDto> getContactInfo() {

        // Перевірка ситуації, коли контактна інформація повністю відсутня (Критерій приймання)
        if (email.isEmpty() && phone.isEmpty() && address.isEmpty()) {
            return ResponseEntity.noContent().build(); // Повертає статус 204
        }

        ContactDto contacts = new ContactDto(email, phone, address, workingHours);
        return ResponseEntity.ok(contacts);
    }
}