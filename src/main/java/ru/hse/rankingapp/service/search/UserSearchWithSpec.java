package ru.hse.rankingapp.service.search;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.dto.user.rating.RatingSearchParamsDto;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.CategoryEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по формированию спеки.
 */
@Service
public class UserSearchWithSpec {

    /**
     * Сформировать спеку.
     */
    public Specification<UserEntity> searchWithSpec(UserSearchParamsDto searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getFirstName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("firstName"), searchParams.getFirstName()));
            }

            if (searchParams.getLastName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), searchParams.getLastName()));
            }

            if (searchParams.getMiddleName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("middleName"), searchParams.getMiddleName()));
            }

            if (searchParams.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), searchParams.getEmail()));
            }

            if (searchParams.getGender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), searchParams.getGender()));
            }

            if (searchParams.getAge() != null) {
                LocalDate today = LocalDate.now();

                Integer age = searchParams.getAge();
                LocalDate startDate = today.minusYears(age);
                LocalDate endDate = today.minusYears(age + 1);

                predicates.add(criteriaBuilder.between(root.get("birthDate"), endDate, startDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Сформировать спеку.
     */
    public Specification<UserEntity> searchWithSpec(RatingSearchParamsDto searchParams) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getCategoryEnum() != null) {
                CategoryEnum categoryEnum = searchParams.getCategoryEnum();
                Expression<LocalDate> birthDate = root.get("birthDate");

                Expression<Object> ageInterval = cb.function("age", Object.class, cb.currentDate(), birthDate);
                Expression<Integer> userAge = cb.function("date_part", Integer.class, cb.literal("year"), ageInterval);

                predicates.add(cb.between(userAge, categoryEnum.getFrom(), categoryEnum.getTo()));

            }

            if (searchParams.getGender() != null) {
                predicates.add(cb.equal(root.get("gender"), searchParams.getGender()));
            }

            if (searchParams.getStartsCountFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startsCount"), searchParams.getStartsCountFrom()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
