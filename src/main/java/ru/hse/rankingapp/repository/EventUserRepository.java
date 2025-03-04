package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.entity.EventUserLinkEntity;

@Repository
public interface EventUserRepository extends JpaRepository<EventUserLinkEntity, Long>, JpaSpecificationExecutor<EventUserLinkEntity> {
}
