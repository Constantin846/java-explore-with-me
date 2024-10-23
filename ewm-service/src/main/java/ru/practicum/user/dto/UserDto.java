package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;


@Data
@EqualsAndHashCode(callSuper = true, of = "email")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto extends UserShortDto {

    @NotBlank(message = "User email must not be blank")
    @Email(message = "Invalid email pattern")
    @Length(message = "User email length must be between 6 and 254 characters inclusive", min = 6, max = 254)
    String email;
}
