package ru.hse.rankingapp.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.SignUpOrganizationRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.enums.Role;

/**
 * Маппер для работы с сущностью организации.
 */
@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CompetitionMapper.class},
        imports = Role.class,
        injectionStrategy = InjectionStrategy.SETTER
)
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

    /**
     * Получить полную информацию об организации.
     *
     * @param organization cущность организации
     * @return Информация об организации
     */
    @Mapping(source = "users", target = "users", qualifiedByName = "mapUsers")
    @Mapping(source = "competitionEntities", target = "competitions", qualifiedByName = "mapCompetitions")
    OrganizationFullInfoDto mapToOrganizationFullInfoDto(OrganizationEntity organization);
}
