package ru.practicum.event.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.Category;
import ru.practicum.user.User;

import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "description")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "annotation", nullable = false)
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    @Column(name = "created_on", nullable = false)
    Instant createdOn;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "event_date", nullable = false)
    Instant eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User initiator;

    @Column(name = "location_lat", nullable = false)
    Double locationLat;

    @Column(name = "location_lon", nullable = false)
    Double locationLon;

    @Column(name = "paid", nullable = false)
    Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    Integer participantLimit;

    @Column(name = "published_on")
    Instant publishedOn;

    @Column(name = "request_moderation", nullable = false)
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    State state;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "views")
    long views;
}
