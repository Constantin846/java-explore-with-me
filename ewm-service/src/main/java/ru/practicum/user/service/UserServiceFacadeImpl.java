package ru.practicum.user.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.stats.StatsService;
import ru.practicum.user.dto.FindUsersContext;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceFacadeImpl implements UserServiceFacade { //todo delete
    private final UserService userService;
    private final StatsService statsService;

    @Override
    public List<UserDto> findUsers(FindUsersContext context, HttpServletRequest request) {
        statsService.create(request);
        return userService.findUsers(context);
    }

    @Override
    public UserDto create(UserDto userDto, HttpServletRequest request) {
        statsService.create(request);
        return userService.create(userDto);
    }

    @Override
    public void delete(long userId, HttpServletRequest request) {
        statsService.create(request);
        userService.delete(userId);
    }
}
