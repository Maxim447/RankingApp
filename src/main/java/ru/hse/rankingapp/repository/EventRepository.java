package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.EventEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью мероприятий.
 */
@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    /**
     * Найти заплыв по uuid.
     */
    @Query(value = """
            select ee from EventEntity ee
            left join fetch ee.eventUserLinks eul
            left join fetch eul.user
            where ee.eventUuid = :uuid
            """)
    Optional<EventEntity> findByUuid(@Param(value = "uuid") UUID uuid);
}
