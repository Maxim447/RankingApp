package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.TokenEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    @Query(value = """
            select te from TokenEntity te
            where te.id = :uuid
            and te.actionIndex <> ru.hse.rankingapp.entity.enums.ActionIndex.D
            """)
    Optional<TokenEntity> findByUuid(@Param(value = "uuid") UUID uuid);
}
