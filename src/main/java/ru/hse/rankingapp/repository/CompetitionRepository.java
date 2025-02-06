package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.CompetitionEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью соревнований.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Long>, JpaSpecificationExecutor<CompetitionEntity> {

    /**
     * Найти соревнование по почте пользователя и uuid соревнования.
     *
     * @param email           почта пользователя
     * @param competitionUUID uuid соревнования
     * @return Сущность соревнования
     */
    @Query(value = """
            select c from CompetitionEntity c
            left join c.organization o
            where c.competitionUuid = :competitionUUID
            and o.email = :email
            """)
    Optional<CompetitionEntity> findByOrganizationEmailAndCompetitionUuid(
            @Param(value = "email") String email, @Param(value = "competitionUUID") UUID competitionUUID);


    /**
     * Найти {@link CompetitionEntity}.
     *
     * @param uuid UUID соревнования
     * @return {@link CompetitionEntity}
     */
    @Query(value = """
            select ce from CompetitionEntity ce
            join fetch ce.eventEntities
            join fetch ce.organization
            where ce.competitionUuid = :uuid
            """)
    Optional<CompetitionEntity> findByCompetitionUuid(@Param(value = "uuid") UUID uuid);
}
