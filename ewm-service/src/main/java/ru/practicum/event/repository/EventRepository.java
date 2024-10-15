package ru.practicum.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(long userId, PageRequest page);

    List<Event> findAllBy(Specification<Event> specification, PageRequest page);

    //List<Event> findAllBy(Specification<Event> specification, PageRequest page);

    @Modifying
    @Query("update Event e set e.views = e.views + 1 where e.id in :ids")
    void incrementViewsEvents(@Param("ids") List<Long> ids);
}
