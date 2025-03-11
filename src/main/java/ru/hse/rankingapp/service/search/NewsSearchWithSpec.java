package ru.hse.rankingapp.service.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.NewsEntity;

import java.time.LocalDate;

/**
 * Сервис по формированию спеки.
 */
@Service
public class NewsSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<NewsEntity> searchWithSpec() {
        return (root, query, criteriaBuilder) -> {
            LocalDate currentDate = LocalDate.now();

            return criteriaBuilder.between(criteriaBuilder.literal(currentDate), root.get("startDate"), root.get("endDate"));
        };
    }
}
