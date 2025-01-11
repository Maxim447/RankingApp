package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.SignUpOrganizationRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.enums.Role;

/**
 * Маппер для работы с сущностью организации.
 */
@Mapper(componentModel = "spring", imports = Role.class)
public interface OrganizationMapper {

    /**
     * Маппинг сущности запроса для регистрации в сущность пользователя.
     *
     * @param signUpRequestDto запрос для регистрации
     * @return сущность пользователя
     */
    @Mapping(source = "organizationEmail", target = "email")
    @Mapping(source = "organizationName", target = "name")
    @Mapping(target = "role", expression = "java(Role.ORGANIZATION)")
    OrganizationEntity signUpRequestDtoToOrganization(SignUpOrganizationRequestDto signUpRequestDto);

    /**
     * Получить информацию об организации.
     *
     * @param organization cущность организации
     * @return Информация об организации
     */
    OrganizationInfoDto mapToOrganizationInfoDto(OrganizationEntity organization);
}
