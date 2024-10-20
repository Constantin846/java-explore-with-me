package ru.practicum.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.user.User;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDtoMapper MAPPER = Mappers.getMapper(UserDtoMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDto(List<User> users);
}
