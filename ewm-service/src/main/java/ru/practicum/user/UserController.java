package ru.practicum.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.FindUsersContext;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserServiceFacade;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserServiceFacade userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findUsers(@Valid FindUsersContext context, HttpServletRequest request) {
        log.info("Find users {}:", context);
        return userService.findUsers(context, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        log.info("Create user {}:", userDto);
        return userService.create(userDto, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long userId, HttpServletRequest request) {
        log.info("Delete user {}:", userId);
        userService.delete(userId, request);
    }
}
