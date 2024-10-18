package ru.practicum.category.service;

import ru.practicum.PageParams;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto, long catId);

    void delete(long catId);

    List<CategoryDto> findAll(PageParams params);

    CategoryDto findById(long catId);
}
