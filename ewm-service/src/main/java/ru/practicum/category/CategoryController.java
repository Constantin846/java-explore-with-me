package ru.practicum.category;

import jakarta.validation.Valid;
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
import ru.practicum.PageParams;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private static final String ADMIN = "/admin";
    private static final String CATEGORIES = "/categories";
    private static final String CATEGORY_ID = "/{category-id}";

    @PostMapping(ADMIN + CATEGORIES)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Request: create category: {}", categoryDto);
        return categoryService.create(categoryDto);
    }

    @PatchMapping(ADMIN + CATEGORIES + CATEGORY_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto,
                              @PathVariable(CATEGORY_ID) long catId) {
        log.info("Request: update category(id={}): {}", catId, categoryDto);
        return categoryService.update(categoryDto, catId);
    }

    @DeleteMapping(ADMIN + CATEGORIES + CATEGORY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(CATEGORY_ID) long catId) {
        log.info("Request: delete category by id: {}", catId);
        categoryService.delete(catId);
    }

    @GetMapping(CATEGORIES)
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findAll(@Valid PageParams params) {
        log.info("Request: find categories by params: {}", params);
        return categoryService.findAll(params);
    }

    @GetMapping(CATEGORIES + CATEGORY_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable(CATEGORY_ID) long catId) {
        log.info("Request: find category by id: {}", catId);
        return categoryService.findById(catId);
    }
}
