package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.TrainerEntity;

/**
 * Репозиторий для работы с тренерами.
 */
@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>, JpaSpecificationExecutor<TrainerEntity> {
}
