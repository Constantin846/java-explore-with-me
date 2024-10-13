package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.dto.FindUsersContext;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.mapper.UserDtoMapper;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDtoMapper mapper;
    private final UserRepository repository;

    @Override
    public List<UserDto> findUsers(FindUsersContext context) {
        List<User> users = repository.findByIdLimitOffset(context.getIds(), context.getSize(), context.getFrom());
        return mapper.toUserDto(users);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = mapper.toUser(userDto);
        user = repository.save(user);
        return mapper.toUserDto(getUserById(user.getId()));
    }

    @Override
    public void delete(long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            String message = String.format("User was not found by id: %d", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    private User getUserById(long userId) {
        return repository.findById(userId).orElseThrow(() -> {
            String message = String.format("User was not found by id: %d", userId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }
}
