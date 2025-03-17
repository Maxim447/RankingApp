package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.CoordinateEntity;
import ru.hse.rankingapp.entity.TrainerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по формированию спеки.
 */
@Service
public class TrainerSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<TrainerEntity> searchWithSpec(Long coordinateId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<TrainerEntity, CoordinateEntity> join = root.join("coordinate", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(join.get("id"), coordinateId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
