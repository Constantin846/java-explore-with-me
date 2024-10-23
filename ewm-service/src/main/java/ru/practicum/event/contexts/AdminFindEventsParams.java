package ru.practicum.event.contexts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.PageParams;

import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class AdminFindEventsParams extends PageParams {

    private List<Long> users;

    private List<String> states;

    List<Long> categories;

    String rangeStart;

    String rangeEnd;

    List<Long> locationTypes;

    List<String> locationNames;

    Double radius;

    Double lat;

    Double lon;
}
