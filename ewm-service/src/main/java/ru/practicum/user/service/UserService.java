package ru.practicum.user.service;

import ru.practicum.user.dto.FindUsersContext;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findUsers(FindUsersContext context);

    UserDto create(UserDto userDto);

    void delete(long userId);
}
