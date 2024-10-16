package ru.practicum.compilation.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationDtoMapper {
    CompilationDtoMapper MAPPER = Mappers.getMapper(CompilationDtoMapper.class);

    CompilationDto toCompilationDto(Compilation compilation);

    Compilation toCompilation(CompilationDto compilationDto); //todo

    @Mapping(target = "events", qualifiedByName = "toEvents")
    Compilation toCompilation(CompilationDtoRequest newCompilationDto);

    default List<CompilationDto> toCompilationDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(this::toCompilationDto)
                .toList();
    }

    @Named("toEvents")
    default List<Event> toEvents(List<Long> eventIds) {
        return eventIds.stream()
                .map(id -> {
                    Event event = new Event();
                    event.setId(id);
                    return event;
                }).toList();
    }
}
