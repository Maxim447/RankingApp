package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.CoordinateEntity;

/**
 * Репозиторий для работы с сущностями координат.
 */
@Repository
public interface CoordinateRepository extends JpaRepository<CoordinateEntity, Long> {
}
