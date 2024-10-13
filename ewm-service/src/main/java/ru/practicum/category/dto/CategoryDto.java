package ru.practicum.category.dto;

import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {

    @Null(message = "Category id must not be set to create")
    Long id;

    @Length(message = "Event title length must be between 1 and 50 characters inclusive", min = 1, max = 50)
    String name;
}
