package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    //List<Event> findAllByInitiator(User initiator, PageRequest page); // todo delete

    //List<Event> findAll(Specification<Event> spec, PageRequest page);

    //List<Event> findAllBy(Specification<Event> specification, PageRequest page);

    @Deprecated
    @Modifying(flushAutomatically = true)
    @Query(value = "update Event e set e.views = e.views + 1 where e.id in :ids")
    void incrementViewsEvents(@Param("ids") List<Long> ids);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Event e set e.views = :views  where e.id in :id")
    void saveViews(long id, int views);
}
