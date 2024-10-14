package ru.practicum.event.contexts;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.PageParams;

import java.util.List;

@Getter
@Setter
public class AdminFindEventsParams extends PageParams {

    private List<Long> users;

    private List<String> states;

    protected List<Long> categories;

    protected String rangeStart;

    protected String rangeEnd;
}
