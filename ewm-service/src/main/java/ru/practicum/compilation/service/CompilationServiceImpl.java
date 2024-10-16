package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.CompilationRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.compilation.dto.CompilationParams;
import ru.practicum.compilation.dto.mapper.CompilationDtoMapper;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationDtoMapper mapper;

    @Override
    public CompilationDto create(CompilationDtoRequest compilationDto) {
        Compilation compilation = mapper.toCompilation(compilationDto);
        compilation = compilationRepository.save(compilation);
        return mapper.toCompilationDto(getCompilationById(compilation.getId()));
    }

    @Override
    public void delete(long compId) {
        if (checkCompilationExists(compId)) {
            compilationRepository.deleteById(compId);
        }
    }

    @Override
    public CompilationDto update(long compId, CompilationDtoRequest compilationDto) {
        if (checkCompilationExists(compId)) {
            Compilation compilation = mapper.toCompilation(compilationDto);
            compilation.setId(compId);
            compilation = compilationRepository.save(compilation);
            return mapper.toCompilationDto(getCompilationById(compilation.getId()));
        }
        return null;
    }

    @Override
    public List<CompilationDto> findAll(CompilationParams params) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(
                params.isPinned(), PageRequest.of(params.getFrom(), params.getSize()));
        return mapper.toCompilationDto(compilations);
    }

    @Override
    public CompilationDto findById(long compId) {
        return mapper.toCompilationDto(getCompilationById(compId));
    }

    private boolean checkCompilationExists(long compId) {
        if (compilationRepository.existsById(compId)) return true;
        String message = String.format("Compilation was not found by id: %s", compId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    private Compilation getCompilationById(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            String message = String.format("Compilation was not found by id: %s", compId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }
}
