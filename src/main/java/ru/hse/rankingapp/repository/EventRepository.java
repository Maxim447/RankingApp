package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.EventEntity;

/**
 * Репозиторий для работы с сущностью мероприятий.
 */
@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
