package ru.practicum.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    CategoryDtoMapper MAPPER = Mappers.getMapper(CategoryDtoMapper.class);

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    default List<CategoryDto> toCategoryDto(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryDto)
                .toList();
    }
}
