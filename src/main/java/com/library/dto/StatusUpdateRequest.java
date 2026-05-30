package com.library.dto;

/**
 * DTO для отримання нового ID статусу від бібліотекаря.
 */
public class StatusUpdateRequest {
    private int statusId;

    // Порожній конструктор для Jackson
    public StatusUpdateRequest() {}

    public StatusUpdateRequest(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }
}