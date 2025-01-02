package ru.hse.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.authservice.dto.user.SignUpUserRequestDto;
import ru.hse.authservice.dto.user.UserInfoDto;
import ru.hse.authservice.entity.User;
import ru.hse.commonmodule.enums.Role;

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
