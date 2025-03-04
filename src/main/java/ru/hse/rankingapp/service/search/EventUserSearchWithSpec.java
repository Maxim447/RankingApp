package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.event.EventUserSearchRequestDto;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.CategoryEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис по формированию спеки.
 */
@Service
public class EventUserSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<EventUserLinkEntity> searchWithSpec(EventUserSearchRequestDto searchParams, UUID eventUuid) {
        return (root, query, cb) -> {
            Join<EventUserLinkEntity, UserEntity> user = root.join("user", JoinType.LEFT);
            Join<EventUserLinkEntity, EventEntity> event = root.join("event", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(event.get("eventUuid"), eventUuid));

            Integer age = searchParams.getAge();
            if (age != null) {
                LocalDate today = LocalDate.now();

                LocalDate startDate = today.minusYears(age);
                LocalDate endDate = today.minusYears(age + 1);
                Expression<LocalDate> birthDate = user.get("birthDate");

                predicates.add(cb.between(birthDate, startDate, endDate));
            }

            if (searchParams.getGender() != null) {
                predicates.add(cb.equal(user.get("gender"), searchParams.getGender().name()));
            }

            if (searchParams.getCategory() != null) {
                CategoryEnum category = searchParams.getCategory();

                Expression<LocalDate> birthDate = user.get("birthDate");

                Expression<Object> ageInterval = cb.function("age", Object.class, cb.currentDate(), birthDate);
                Expression<Integer> userAge = cb.function("date_part", Integer.class, cb.literal("year"), ageInterval);

                predicates.add(cb.between(userAge, category.getFrom(), category.getTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
