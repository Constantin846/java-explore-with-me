package ru.practicum.event.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;

import java.time.Instant;
import java.util.List;

public class EventSpecifications {
    private EventSpecifications() {}

    public static Specification<Event> hasUserIdEquals(List<Long> userIds) {
        return ((root, query, criteriaBuilder) -> root.get("user_id").in(userIds));
    }

    public static Specification<Event> hasStateEquals(List<String> states) {
        return ((root, query, criteriaBuilder) -> root.get("state").in(states));
    }

    public static Specification<Event> hasCategoryIdEquals(List<Long> categoryIds) {
        return ((root, query, criteriaBuilder) -> root.get("category_id").in(categoryIds));
    }

    public static Specification<Event> hasEventDateAfter(Instant rangeStart) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("event_date"), rangeStart));
    }

    public static Specification<Event> hasEventDateBefore(Instant rangeEnd) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("event_date"), rangeEnd));
    }

    public static Specification<Event> hasTextInAnnotation(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("annotation"), "%" + text + "%"));
    }

    public static Specification<Event> hasTextInDescription(String text) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%" + text + "%"));
    }

    public static Specification<Event> hasPaidIsTrue(boolean paid) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid));
    }

    public static Specification<Event> hasAvailableIsTrue() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("participant_limit"), root.get("confirmed_requests")));
    }

   /* public static Specification<Event> hasSortByEventDate() {
        return ((root, query, criteriaBuilder) -> {
             query.orderBy(criteriaBuilder.asc(root.get("event_date")));

        }

    }*/ //todo

}
