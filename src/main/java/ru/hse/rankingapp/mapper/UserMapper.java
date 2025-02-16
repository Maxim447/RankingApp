package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.hse.rankingapp.dto.user.SignUpUserRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.entity.UserEntity;

import java.util.List;
import java.util.Set;

/**
 * Маппер для работы с сущностью пользователя.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Маппинг сущности запроса для регистрации в сущность пользователя.
     *
     * @param signUpRequestDto запрос для регистрации
     * @return сущность пользователя
     */
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
}
