package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.OrganizationEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью организации.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long>, JpaSpecificationExecutor<OrganizationEntity> {

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    @Query(value = """
            select o from OrganizationEntity o
            where o.email = :email
            """)
    Optional<OrganizationEntity> findByEmailOpt(@Param(value = "email") String email);

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    OrganizationEntity findByEmail(String email);

    /**
     * Проверить наличие записи по электронной почте.
     *
     * @param email почта
     * @return true/false
     */
    boolean existsByEmail(String email);

    /**
     * Изменить поле email по id.
     *
     * @param oldEmail идентификатор пользователя
     * @param email    электронная почта
     */
    @Modifying
    @Query(value = """
            UPDATE OrganizationEntity o
            SET o.email = :email, o.modifyDttm = current timestamp, o.actionIndex = 'U'
            WHERE o.email = :oldEmail
            """)
    void updateEmailByOldEmail(@Param(value = "oldEmail") String oldEmail, @Param(value = "email") String email);

    /**
     * Изменить поле isOpen по id.
     *
     * @param email  Почта
     * @param isOpen статус открытости
     */
    @Modifying
    @Query(value = """
            UPDATE OrganizationEntity o
            SET o.isOpen = :isOpen, o.modifyDttm = current timestamp, o.actionIndex = 'U'
            WHERE o.email = :email
            """)
    void updateOpenStatusByEmail(@Param(value = "email") String email, @Param(value = "isOpen") Boolean isOpen);

    /**
     * Найти организацию по почте.
     *
     * @param email Почта
     * @return Сущность организации
     */
    @Query(value = """
            select o from OrganizationEntity o
            left join fetch o.users
            left join fetch o.competitionEntities
            where o.email = :email
            """)
    OrganizationEntity findByEmailWithFetch(@Param(value = "email") String email);
}
