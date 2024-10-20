package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.compilation.dto.CompilationParams;

import java.util.List;

public interface CompilationService {
    CompilationDto create(CompilationDtoRequest compilationDto);

    void delete(long compId);

    CompilationDto update(long compId, CompilationDtoRequest compilationDto);

    List<CompilationDto> findAll(CompilationParams params);

    CompilationDto findById(long compId);
}
