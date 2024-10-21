package ru.practicum.location;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationTypeRepository extends JpaRepository<LocationType, Long> {
    List<LocationType> findAllBy(PageRequest request);
}
