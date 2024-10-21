package ru.practicum.event.contexts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.PageParams;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminFindEventsParams extends PageParams {

    private List<Long> users;

    private List<String> states;

    protected List<Long> categories;

    protected String rangeStart;

    protected String rangeEnd;

    protected List<Long> locationTypes;

    protected List<String> locationNames;

    protected Double radius;

    protected Double lat;

    protected Double lon;
}
