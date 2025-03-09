package ru.hse.rankingapp.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.coordinates.CoordinateProjectionDto;
import ru.hse.rankingapp.entity.CoordinateEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностями координат.
 */
@Repository
public interface CoordinateRepository extends JpaRepository<CoordinateEntity, Long> {

    @Query("""
            select new ru.hse.rankingapp.dto.coordinates.CoordinateProjectionDto(
            ce.id, ce.name, ce.description, ce.geometry
            )
            from CoordinateEntity ce
            """)
    @QueryHints({
            @QueryHint(name = "org.hibernate.fetchSize", value = "200"),
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
            @QueryHint(name = "org.hibernate.cacheMode", value = "IGNORE"),
    })
    List<CoordinateProjectionDto> findAllCoordinates();

    @Transactional
    @Modifying
    @Query("DELETE FROM CoordinateEntity ce WHERE ce.id = :id")
    void deleteCoordinateById(@Param("id") Long id);
}
