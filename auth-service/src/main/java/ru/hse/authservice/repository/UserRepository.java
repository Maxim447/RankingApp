package ru.hse.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.authservice.entity.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью пользователя.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Получить сущность пользователя по почте.
     *
     * @param email почта
     * @return сущность пользователя
     */
    Optional<User> findByEmail(String email);

    /**
     * Получить сущность пользователя по номеру телефона.
     *
     * @param phone почта
     * @return сущность пользователя
     */
    Optional<User> findByPhone(String phone);

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
     * @param id    идентификатор пользователя
     * @param phone номер телефона
     */
    @Modifying
    @Query(value = """
            UPDATE User u
            SET u.phone = :phone
            WHERE u.id = :id
            """)
    void updatePhoneById(@Param(value = "id") Long id, @Param(value = "phone") String phone);

    /**
     * Изменить поле password по id.
     *
     * @param id       идентификатор пользователя
     * @param password пароль
     */
    @Modifying
    @Query(value = """
            UPDATE User u
            SET u.password = :password, u.modifyDttm = current timestamp, u.actionIndex = 'U'
            WHERE u.id = :id
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
            UPDATE User u
            SET u.email = :email, u.modifyDttm = current timestamp, u.actionIndex = 'U'
            WHERE u.id = :id
            """)
    void updateEmailById(@Param(value = "id") Long id, @Param(value = "email") String email);
}