package ru.practicum.user.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.user.dto.FindUsersContext;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserServiceFacade {
    List<UserDto> findUsers(FindUsersContext context, HttpServletRequest request);

    UserDto create(UserDto userDto, HttpServletRequest request);

    void delete(long userId, HttpServletRequest request);
}
