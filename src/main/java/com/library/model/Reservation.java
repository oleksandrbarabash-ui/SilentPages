package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "reservation_status_id", nullable = false)
    private ReservationStatus status;

    @Column(name = "create_time", nullable = false)
    private LocalDate createTime;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "update_time", nullable = false)
    private LocalDate updateTime;

    public Reservation() {}

    // Геттери та сеттери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public LocalDate getCreateTime() { return createTime; }
    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDate updateTime) { this.updateTime = updateTime; }
}