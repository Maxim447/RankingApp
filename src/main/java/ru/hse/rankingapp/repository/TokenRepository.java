package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.TokenEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с токенами.
 */
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    /**
     * Найти сущность токена.
     *
     * @param uuid UUID токена
     * @return Сущность токена
     */
    @Query(value = """
            select te from TokenEntity te
            where te.id = :uuid
            and te.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    Optional<TokenEntity> findByUuid(@Param(value = "uuid") UUID uuid);

    /**
     * Найти сущность токена вместе с организацией и пользователем.
     *
     * @param uuid UUID токена
     * @return Сущность токена
     */
    @Query(value = """
            select te from TokenEntity te
            left join fetch te.user
            left join fetch te.organization
            where te.id = :uuid
            and te.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    Optional<TokenEntity> findByIdFetched(@Param(value = "uuid") UUID uuid);
}
