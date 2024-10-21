package ru.practicum.event.repository;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;

import java.time.Instant;
import java.util.List;

@UtilityClass
public class EventSpecifications {

    public static Specification<Event> hasUserIdEquals(List<Long> userIds) {
        return ((root, query, criteriaBuilder) -> root.get("initiator").get("id").in(userIds));
    }

    public static Specification<Event> hasStateEquals(List<String> states) {
        return ((root, query, criteriaBuilder) -> root.get("state").in(states));
    }

    public static Specification<Event> hasCategoryIdEquals(List<Long> categoryIds) {
        return ((root, query, criteriaBuilder) -> root.get("category").get("id").in(categoryIds));
    }

    public static Specification<Event> hasEventDateAfter(Instant rangeStart) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
    }

    public static Specification<Event> hasEventDateBefore(Instant rangeEnd) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
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
                criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")));
    }

    public static Specification<Event> hasLocationNames(List<String> locationNames) {
        return ((root, query, criteriaBuilder) -> root.get("locationName").in(locationNames));
    }

    public static Specification<Event> hasLocationTypeEquals(List<Long> locationTypeIds) {
        return ((root, query, criteriaBuilder) -> root.get("locationType").get("id").in(locationTypeIds));
    }

    public static Specification<Event> hasLocationInRadius(Double lat, Double lon, Double radius) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        criteriaBuilder.function("distance", Double.class,
                                criteriaBuilder.literal(lat),
                                criteriaBuilder.literal(lon),
                                root.get("locationLat"),
                                root.get("locationLon")),
                        radius)
                );
    }
}
