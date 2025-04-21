package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.EventUserLinkEntity;

import java.util.Set;

@Repository
public interface EventUserRepository extends JpaRepository<EventUserLinkEntity, Long>, JpaSpecificationExecutor<EventUserLinkEntity> {

    /**
     * Найти все завершенные заплывы пользователя по email.
     */
    @Query(value = """
            select eule from EventUserLinkEntity eule
            left join fetch eule.user u
            left join fetch eule.event e
            left join fetch e.competition
            where u.email = :email and e.status = ru.hse.rankingapp.entity.enums.StatusEnum.ENDED
            """)
    Set<EventUserLinkEntity> findByUserEmail(@Param(value = "email") String email);

}
