package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.PartnerEntity;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {

    /**
     * Удалить партнера по id.
     */
    @Modifying
    @Query("DELETE FROM PartnerEntity pe WHERE pe.id = :id")
    void deletePartnerById(@Param(value = "id") Long id);
}
