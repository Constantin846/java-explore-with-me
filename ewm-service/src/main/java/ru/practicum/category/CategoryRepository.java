package ru.practicum.category;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<CategoryDto> findAllBy(PageRequest request);
}
