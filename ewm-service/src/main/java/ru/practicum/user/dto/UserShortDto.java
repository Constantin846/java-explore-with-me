package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class UserShortDto {

    @Null(message = "User id must not be set")
    Long id;

    @NotBlank(message = "User name must not be blank")
    @Length(message = "User name length must be between 2 and 250 characters inclusive", min = 2, max = 250)
    String name;
}
