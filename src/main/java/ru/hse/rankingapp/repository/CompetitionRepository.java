package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.CompetitionEntity;

/**
 * Репозиторий для работы с сущностью соревнований.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Long> {
}
