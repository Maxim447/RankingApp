package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.entity.OrganizationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по формированию спеки.
 */
@Service
public class OrganizationSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<OrganizationEntity> searchWithSpec(OrganizationSearchParamsDto searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchParams.getName() + "%"));
            }

            if (searchParams.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), searchParams.getEmail()));
            }

            if (searchParams.getIsOpen() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isOpen"), searchParams.getIsOpen()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
