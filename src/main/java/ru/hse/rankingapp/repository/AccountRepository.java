package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.AccountEntity;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    /**
     * Получить сущность аккаунта по почте.
     *
     * @param email Почта
     * @return Сущность аккаунта
     */
    @Query(value = """
            select ae from AccountEntity ae
            join fetch ae.roles
            where ae.email = :email
            """)
    Optional<AccountEntity> findByEmailOpt(@Param(value = "email") String email);

    /**
     * Получить сущность аккаунта по почте.
     *
     * @param email Почта
     * @return Сущность аккаунта
     */
    @Query(value = """
            select ae from AccountEntity ae
            join fetch ae.roles
            where ae.email = :email
            """)
    AccountEntity findByEmail(@Param(value = "email") String email);

    /**
     * Изменить поле password по email.
     *
     * @param email    Почта пользователя
     * @param password пароль
     */
    @Modifying
    @Query(value = """
            UPDATE AccountEntity ae
            SET ae.password = :password
            WHERE ae.email = :email
            """)
    void updatePasswordByEmail(@Param(value = "email") String email, @Param(value = "password") String password);


    boolean existsByEmail(String email);

    /**
     * Изменить поле email по старой почте.
     *
     * @param oldEmail идентификатор пользователя
     * @param email    электронная почта
     */
    @Modifying
    @Query(value = """
            UPDATE AccountEntity ae
            SET ae.email = :email
            WHERE ae.email = :oldEmail
            """)
    void updateEmailByOldEmail(@Param(value = "oldEmail") String oldEmail, @Param(value = "email") String email);
}
