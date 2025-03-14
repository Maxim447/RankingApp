package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.entity.CompetitionEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
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
            left join fetch ce.eventEntities
            left join fetch ce.organization
            where ce.competitionUuid = :uuid
            """)
    Optional<CompetitionEntity> findByCompetitionUuid(@Param(value = "uuid") UUID uuid);

    /**
     * Найти все соревнования на текущую дату.
     *
     * @param now Текущая дата
     * @return Все соревнования на текущую дату
     */
    @Query(value = """
            select ce from CompetitionEntity ce
            left join fetch ce.eventEntities
            where ce.date = :now
            and ce.status = ru.hse.rankingapp.entity.enums.StatusEnum.CREATED
            and ce.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    Set<CompetitionEntity> findAllByCurrentDate(@Param(value = "now") LocalDate now);

    /**
     * Найти все соревнования меньше текущей даты.
     *
     * @param now Текущая дата
     * @return Все соревнования на текущую дату
     */
    @Query(value = """
            select ce.id from CompetitionEntity ce
            where ce.date < :now
            and ce.status = ru.hse.rankingapp.entity.enums.StatusEnum.IN_PROGRESS
            and ce.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    Set<Long> findAllLowerCurrentDate(@Param(value = "now") LocalDate now);

    /**
     * Обновить статус соревнований.
     *
     * @param competitionIds Id соревнований.
     */
    @Modifying
    @Transactional
    @Query(value = """
            update CompetitionEntity ce
            set ce.status = 'ENDED', ce.modifyDttm = current timestamp, ce.actionIndex = 'U'
            where ce.id in :ids
            and ce.status = ru.hse.rankingapp.entity.enums.StatusEnum.IN_PROGRESS
            and ce.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    void updateStatus(@Param(value = "ids") Set<Long> competitionIds);

    /**
     * Получить соревнование и почту организации по uuid.
     *
     * @param competitionUuid Uuid соревнования
     * @return соревнование и почта организации.
     */
    @Query(value = """
            select ce from CompetitionEntity ce
            left join fetch ce.organization o
            left join fetch ce.eventEntities ee
            left join fetch ee.eventUserLinks
            left join fetch ce.competitionUserLinkEntities
            where ce.competitionUuid = :uuid
            """)
    Optional<CompetitionEntity> findByUuidWithOrganization(@Param(value = "uuid") UUID competitionUuid);

    /**
     * Записан ли пользователь на соревнование.
     */
    @Query(value = """
            SELECT EXISTS(
            SELECT 1 FROM competition_user_link
                     WHERE user_id = :userId AND competition_id = :competitionId
            )
            """, nativeQuery = true)
    boolean userExistsInCompetition(@Param(value = "competitionId") Long competitionId, @Param(value = "userId") Long userId);
}
