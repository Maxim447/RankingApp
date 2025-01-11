package ru.hse.rankingapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;

/**
 * Маппер для работы с сущностью соревнований.
 */
@Mapper(componentModel = "spring")
public interface CompetitionMapper {

    /**
     * Получить сущность соревнования.
     *
     * @param organization Сущность организации
     * @param competition Дто для создания соревнования
     * @return сущность соревнования
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "competition.competitionLocation", target = "location")
    @Mapping(source = "competition.competitionName", target = "name")
    @Mapping(source = "competition.competitionDate", target = "date")
    @Mapping(source = "competition.maxParticipants", target = "maxParticipants")
    @Mapping(source = "competition.competitionType", target = "competitionType")
    @Mapping(source = "organization", target = "organization")
    CompetitionEntity toCompetitionEntity(OrganizationEntity organization, CreateCompetitionDto competition);
}
