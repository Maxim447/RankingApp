package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.competition.CompetitionSearchParamsDto;
import ru.hse.rankingapp.entity.CompetitionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по формированию спеки для соревнований.
 */
@Service
public class CompetitionSearchWithSpec {

    /**
     * Сформировать спеку для соревнований.
     */
    public Specification<CompetitionEntity> searchWithSpec(CompetitionSearchParamsDto searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchParams.getName() + "%"));
            }

            if (searchParams.getLocation() != null) {
                predicates.add(criteriaBuilder.like(root.get("location"), "%" + searchParams.getLocation() + "%"));
            }

            if (searchParams.getCompetitionType() != null) {
                predicates.add(criteriaBuilder.like(root.get("competitionType"), "%" + searchParams.getCompetitionType() + "%"));
            }

            if (searchParams.getDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), searchParams.getDate()));
            }

            if (searchParams.getMinParticipants() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxParticipants"), searchParams.getMinParticipants()));
            }

            if (searchParams.getMaxParticipants() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxParticipants"), searchParams.getMaxParticipants()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
