package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    Optional<OrganizationEntity> findByEmail(String email);

    /**
     * Проверить наличие записи по электронной почте.
     *
     * @param email почта
     * @return true/false
     */
    boolean existsByEmail(String email);

    /**
     * Изменить поле password по id.
     *
     * @param id       идентификатор пользователя
     * @param password пароль
     */
    @Modifying
    @Query(value = """
            UPDATE OrganizationEntity o
            SET o.password = :password, o.modifyDttm = current timestamp, o.actionIndex = 'U'
            WHERE o.id = :id
            """)
    void updatePasswordById(@Param(value = "id") Long id, @Param(value = "password") String password);

    /**
     * Изменить поле email по id.
     *
     * @param id    идентификатор пользователя
     * @param email электронная почта
     */
    @Modifying
    @Query(value = """
            UPDATE OrganizationEntity o
            SET o.email = :email, o.modifyDttm = current timestamp, o.actionIndex = 'U'
            WHERE o.id = :id
            """)
    void updateEmailById(@Param(value = "id") Long id, @Param(value = "email") String email);
}
