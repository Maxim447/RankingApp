package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.dto.aboutus.AboutUsInfoDto;
import ru.hse.rankingapp.entity.AboutUsEntity;

@Repository
public interface AboutUsRepository extends JpaRepository<AboutUsEntity, Long> {

    @Modifying
    @Query("""
            update AboutUsEntity aue
            set aue.description = :description
            where aue.id = :id
            """)
    void updateTextById(@Param(value = "id") long id, @Param(value = "description") String description);

    /**
     * Найти описание по id.
     */
    @Query("""
            select new ru.hse.rankingapp.dto.aboutus.AboutUsInfoDto(aue.description) from AboutUsEntity aue
            where aue.id = :id
            """)
    AboutUsInfoDto findDescriptionById(@Param(value = "id") long id);
}
