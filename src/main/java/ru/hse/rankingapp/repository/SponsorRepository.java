package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.SponsorEntity;

@Repository
public interface SponsorRepository extends JpaRepository<SponsorEntity, Long> {

    /**
     * Удалить спонсора по id.
     */
    @Modifying
    @Query("DELETE FROM SponsorEntity se WHERE se.id = :id")
    void deleteSponsorById(@Param(value = "id") Long id);
}
