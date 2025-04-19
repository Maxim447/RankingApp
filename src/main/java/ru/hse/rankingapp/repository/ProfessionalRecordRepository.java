package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.ProfessionalRecordEntity;
import ru.hse.rankingapp.entity.enums.Gender;

import java.util.Optional;

/**
 * Репозиторий для работы с рекордами.
 */
@Repository
public interface ProfessionalRecordRepository extends JpaRepository<ProfessionalRecordEntity, Long>, JpaSpecificationExecutor<ProfessionalRecordEntity> {

    /**
     * Обновить время по id.
     */
    @Modifying
    @Query(value = """
            update ProfessionalRecordEntity pre
            set pre.time = :time
            where pre.id = :id
            """)
    void updateTimeById(@Param(value = "time") Long time, @Param(value = "id") Long recordId);

    /**
     * Найти рекорд по уникальным параметрам.
     */
    @Query(value = """
            select pre from ProfessionalRecordEntity pre
            where pre.distance = :distance and pre.gender = :gender and pre.style = :style
            """)
    Optional<ProfessionalRecordEntity> findRecordTimeByGenderDistanceStyle(@Param(value = "distance") Integer distance,
            @Param(value = "gender") Gender gender, @Param(value = "style") String style);
}
