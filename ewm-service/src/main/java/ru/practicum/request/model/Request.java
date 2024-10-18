package ru.practicum.request.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "requests")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created", nullable = false)
    Instant created;

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "event_id", nullable = false)
    Long event;

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id", nullable = false)
    Long requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;
}
