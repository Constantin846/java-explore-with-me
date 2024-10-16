package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoRequest;
import ru.practicum.compilation.dto.CompilationParams;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.validation.groups.CreateValid;
import ru.practicum.validation.groups.UpdateValid;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService compilationService;
    private static final String ADMIN = "/admin";
    private static final String COMPILATIONS = "/compilations";
    private static final String COMPILATION_ID = "comp-id";
    private static final String COMPILATION_ID_PATH = "/{comp-id}";

    @PostMapping(ADMIN + COMPILATIONS)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Validated(CreateValid.class) @RequestBody CompilationDtoRequest compilationDto) {
        log.info("Request: create compilation: {}", compilationDto);
        return compilationService.create(compilationDto);
    }

    @DeleteMapping(ADMIN + COMPILATIONS + COMPILATION_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(COMPILATION_ID) long compId) {
        log.info("Request: delete compilation: {}", compId);
        compilationService.delete(compId);
    }

    @PatchMapping(ADMIN + COMPILATIONS + COMPILATION_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompilationDto update(@PathVariable(COMPILATION_ID) long compId,
                                 @Validated(UpdateValid.class) @RequestBody CompilationDtoRequest compilationDto) {
        log.info("Request: update compilation: {}", compId);
        return compilationService.update(compId, compilationDto);
    }

    @GetMapping(COMPILATIONS)
    public List<CompilationDto> findAll(CompilationParams params) {
        log.info("Request: find compilations with params: {}", params);
        return compilationService.findAll(params);
    }

    @GetMapping(COMPILATIONS + COMPILATION_ID_PATH)
    public CompilationDto findById(@PathVariable(COMPILATION_ID) long compId) {
        log.info("Request: find compilation by id: {}", compId);
        return compilationService.findById(compId);
    }
}
