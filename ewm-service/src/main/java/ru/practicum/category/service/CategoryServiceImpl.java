package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageParams;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_WAS_NOT_FOUND_BY_ID = "Category was not found by id: %s";
    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper mapper;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        return save(categoryDto);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto, long catId) {
        Category oldCat = getCategoryById(catId);
        categoryDto.setId(oldCat.getId());
        return save(categoryDto);
    }

    @Override
    @Transactional
    public void delete(long catId) {
        if (checkCategoryExists(catId)) {
            categoryRepository.deleteById(catId);
        }
    }

    @Override
    public List<CategoryDto> findAll(PageParams params) {
        List<Category> categories = categoryRepository.findAllBy(PageRequest.of(params.getFrom(), params.getSize()));
        return mapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto findById(long catId) {
        return mapper.toCategoryDto(getCategoryById(catId));
    }

    private CategoryDto save(CategoryDto categoryDto) {
        Category category = mapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        return mapper.toCategoryDto(getCategoryById(category.getId()));
    }

    private Category getCategoryById(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            String message = String.format(CATEGORY_WAS_NOT_FOUND_BY_ID, catId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private boolean checkCategoryExists(long catId) {
        if (categoryRepository.existsById(catId)) return true;
        String message = String.format(CATEGORY_WAS_NOT_FOUND_BY_ID, catId);
        log.warn(message);
        throw new NotFoundException(message);
    }
}
