package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            select u
            from User u
            where u.id in :ids
            order by u.id
            limit :size
            offset :from
            """)
    List<User> findByIdLimitOffset(@Param("ids") List<Long> ids,
                                   @Param("size") int size,
                                   @Param("from") int from);
}
