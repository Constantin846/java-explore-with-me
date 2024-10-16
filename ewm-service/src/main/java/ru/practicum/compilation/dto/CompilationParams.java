package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.PageParams;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationParams extends PageParams {

    boolean pinned;
}
