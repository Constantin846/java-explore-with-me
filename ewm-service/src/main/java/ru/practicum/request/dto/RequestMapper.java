package ru.practicum.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper MAPPER = Mappers.getMapper(RequestMapper.class);

    RequestDto toRequestDto(Request request);

    Request toRequest(RequestDto requestDto);

    default List<RequestDto> toRequestDto(List<Request> requests) {
        return requests.stream()
                .map(this::toRequestDto)
                .toList();
    }

    default Set<RequestDto> toRequestDto(Set<Request> requests) {
        return requests.stream()
                .map(this::toRequestDto)
                .collect(Collectors.toSet());
    }
}
