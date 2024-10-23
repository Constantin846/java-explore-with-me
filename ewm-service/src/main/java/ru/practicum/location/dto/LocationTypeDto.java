package ru.practicum.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationTypeDto {

    @Null(message = "Location type id must not be set to create")
    Long id;

    @NotBlank(message = "Location type name must not be blank")
    @Length(message = "Location type name length must be between 1 and 50 characters inclusive", min = 1, max = 50)
    String name;
}
