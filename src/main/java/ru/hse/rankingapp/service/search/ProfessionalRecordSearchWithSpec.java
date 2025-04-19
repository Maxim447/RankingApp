package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.professional.records.RecordSearchParamsDto;
import ru.hse.rankingapp.entity.ProfessionalRecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по формированию спеки.
 */
@Service
public class ProfessionalRecordSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<ProfessionalRecordEntity> searchWithSpec(RecordSearchParamsDto searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getDistance() != null) {
                predicates.add(criteriaBuilder.equal(root.get("distance"), searchParams.getDistance()));
            }

            if (searchParams.getStyle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("style"), searchParams.getStyle()));
            }

            if (searchParams.getGender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), searchParams.getGender().name()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
