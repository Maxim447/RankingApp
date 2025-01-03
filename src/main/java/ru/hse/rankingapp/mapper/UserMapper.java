package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.user.SignUpUserRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.entity.User;
import ru.hse.rankingapp.entity.enums.Role;

/**
 * Маппер для работы с сущностью пользователя.
 */
@Mapper(componentModel = "spring", imports = Role.class)
public interface UserMapper {

    /**
     * Маппинг сущности запроса для регистрации в сущность пользователя.
     *
     * @param signUpRequestDto запрос для регистрации
     * @return сущность пользователя
     */
    @Mapping(target = "role", expression = "java(Role.USER)")
    User signUpRequestDtoToUser(SignUpUserRequestDto signUpRequestDto);

    /**
     * Получить информацию о пользователе.
     *
     * @param user cущность пользователя
     * @return Информация о пользователе
     */
    UserInfoDto mapToUserInfoDto(User user);
}
