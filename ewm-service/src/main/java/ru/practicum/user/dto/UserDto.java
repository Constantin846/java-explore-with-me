package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {

    @Null(message = "User id must not be set")
    protected Long id;

    @NotBlank(message = "User email must not be blank")
    @Email(message = "Invalid email pattern")
    @Length(message = "User email length must be between 6 and 254 characters inclusive", min = 6, max = 254)
    private String email;

    @NotBlank(message = "User name must not be blank")
    @Length(message = "User name length must be between 2 and 250 characters inclusive", min = 2, max = 250)
    protected String name;
}
