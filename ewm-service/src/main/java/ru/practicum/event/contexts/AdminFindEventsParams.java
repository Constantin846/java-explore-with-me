package ru.practicum.event.contexts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@FieldDefaults(level = AccessLevel.PROTECTED)
public class AdminFindEventsParams extends PageParams {

    private List<Long> users;

    private List<String> states;

    protected List<Long> categories;

    protected String rangeStart;

    protected String rangeEnd;
}
