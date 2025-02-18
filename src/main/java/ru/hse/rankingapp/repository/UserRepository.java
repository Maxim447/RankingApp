package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.UserEntity;

import java.util.Optional;
import java.util.Set;

/**
 * Репозиторий для работы с сущностью пользователя.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    @Query(value = """
            select u from UserEntity u
            where u.email = :email
            """)
    Optional<UserEntity> findByEmailOpt(@Param(value = "email") String email);

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    UserEntity findByEmail(String email);

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    @Query(value = """
            select u from UserEntity u
            left join fetch u.organizations
            left join fetch u.eventUserLinks eul
            left join fetch eul.event
            left join fetch u.competitionUserLinks cul
            left join fetch cul.competitionEntity
            where u.email = :email
            """)
    UserEntity findAllInfoByEmail(@Param(value = "email") String email);

    /**
     * Проверить наличие записи по электронной почте.
     *
     * @param email почта
     * @return true/false
     */
    boolean existsByEmail(String email);

    /**
     * Проверить наличие записи в таблице по номеру телефона.
     *
     * @param phone номер телефона
     * @return true/false
     */
    boolean existsByPhone(String phone);

    /**
     * Изменить поле phone по id.
     *
     * @param email Почта пользователя
     * @param phone номер телефона
     */
    @Modifying
    @Query(value = """
            UPDATE UserEntity u
            SET u.phone = :phone
            WHERE u.email = :email
            """)
    void updatePhoneByEmail(@Param(value = "email") String email, @Param(value = "phone") String phone);

    /**
     * Изменить поле email по id.
     *
     * @param oldEmail Текущая почта пользователя
     * @param email    новая электронная почта
     */
    @Modifying
    @Query(value = """
            UPDATE UserEntity u
            SET u.email = :email, u.modifyDttm = current timestamp, u.actionIndex = 'U'
            WHERE u.email = :oldEmail
            """)
    void updateEmailByOldEmail(@Param(value = "oldEmail") String oldEmail, @Param(value = "email") String email);

    /**
     * Найти пользователей по почтам.
     *
     * @param usersEmails почты
     * @return список пользователей
     */
    @Query(value = """
            select u from UserEntity u
            where u.email in :emails
            """)
    Set<UserEntity> findByEmails(@Param(value = "emails") Set<String> usersEmails);
}