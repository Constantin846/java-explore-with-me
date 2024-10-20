package ru.practicum.event.contexts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindEventsParams extends AdminFindEventsParams {

    String text;

    Boolean paid;

    Boolean onlyAvailable;

    String sort;
}
