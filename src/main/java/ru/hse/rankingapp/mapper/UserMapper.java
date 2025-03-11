package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.hse.rankingapp.dto.user.SignUpUserRequestDto;
import ru.hse.rankingapp.dto.user.UserFullInfoDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.rating.UserRatingDto;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.utils.FioUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Маппер для работы с сущностью пользователя.
 */
@Mapper(componentModel = "spring",
        uses = {
                OrganizationMapper.class,
                EventMapper.class,
                CompetitionMapper.class
        }
)
public interface UserMapper {

    /**
     * Маппинг сущности запроса для регистрации в сущность пользователя.
     *
     * @param signUpRequestDto запрос для регистрации
     * @return сущность пользователя
     */
    @Mapping(target = "rating", constant = "0.0")
    UserEntity signUpRequestDtoToUser(SignUpUserRequestDto signUpRequestDto);

    /**
     * Получить информацию о пользователе.
     *
     * @param user cущность пользователя
     * @return Информация о пользователе
     */
    UserInfoDto mapToUserInfoDto(UserEntity user);

    /**
     * Получить информацию о пользователе.
     *
     * @param users cущность пользователей
     * @return Информация о пользователях
     */
    @Named(value = "mapUsers")
    List<UserInfoDto> mapUserInfoList(Set<UserEntity> users);


    /**
     * Смапить полную информацию о пользователе.
     *
     * @param userEntity Сущность пользователя
     * @return Полная информация о пользователе
     */
    @Mapping(source = "organizations", target = "userOrganizations")
    @Mapping(source = "competitionUserLinks", target = "userCompetitions")
    @Mapping(source = "eventUserLinks", target = "userEvents")
    UserFullInfoDto mapToUserFullInfoDto(UserEntity userEntity);

    /**
     * Смапить рейтинг пользователя.
     *
     * @param entity Сущность
     * @return Информация о рейтинге пользователя
     */
    @Mapping(source = ".", target = "fullName", qualifiedByName = "mapFullName")
    @Mapping(source = "gender.shortValue", target = "gender")
    @Mapping(source = "birthDate", target = "age", qualifiedByName = "mapAge")
    @Mapping(source = "bestAverageTime100", target = "bestTime100", qualifiedByName = "mapTime")
    @Mapping(source = "startsCount", target = "starts")
    UserRatingDto mapUserRatingDto(UserEntity entity);

    /**
     * Маппинг ФИО.
     */
    @Named("mapFullName")
    default String mapFullName(UserEntity user) {
        return FioUtils.buildFullName(user);
    }

    /**
     * Маппинг полного возраста.
     */
    @Named("mapAge")
    default Integer mapAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthDate.getYear();

        // Проверяем, прошел ли день рождения в текущем году
        if (now.getMonthValue() < birthDate.getMonthValue() ||
                (now.getMonthValue() == birthDate.getMonthValue() && now.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }

    /**
     * Маппинг лучшего времени на 100 метров.
     */
    @Named("mapTime")
    default LocalTime mapTime(Long time) {
        if (time == null) {
            return null;
        }

        return LocalTime.ofNanoOfDay(time * 1_000_000);
    }
}
