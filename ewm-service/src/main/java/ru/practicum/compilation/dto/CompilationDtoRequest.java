package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.validation.constraints.NullOrNotBlank;
import ru.practicum.validation.groups.CreateValid;
import ru.practicum.validation.groups.UpdateValid;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoRequest {

    List<Long> events;

    boolean pinned = false;

    @NotBlank(message = "Compilation title must not be blank", groups = CreateValid.class)
    @NullOrNotBlank(message = "Compilation title must be null or not blank", groups = UpdateValid.class)
    @Length(message = "Compilation title length must be between 1 and 50 characters inclusive",
            min = 1, max = 50, groups = {CreateValid.class, UpdateValid.class})
    String title;
}
