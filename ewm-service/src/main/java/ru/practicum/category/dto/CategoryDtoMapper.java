package ru.practicum.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.Category;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    CategoryDtoMapper MAPPER = Mappers.getMapper(CategoryDtoMapper.class);

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);
}
