package ru.hse.rankingapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.hse.rankingapp.dto.competition.CompetitionFullInfoDto;
import ru.hse.rankingapp.dto.competition.CompetitionInfoDto;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;

import java.util.List;
import java.util.Set;

/**
 * Маппер для работы с сущностью соревнований.
 */
@Mapper(componentModel = "spring",
        uses = {EventMapper.class, OrganizationMapper.class},
        injectionStrategy = InjectionStrategy.SETTER
)
public interface CompetitionMapper {

    /**
     * Получить сущность соревнования.
     *
     * @param organization Сущность организации
     * @param competition  Дто для создания соревнования
     * @return сущность соревнования
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "competition.competitionLocation", target = "location")
    @Mapping(source = "competition.competitionName", target = "name")
    @Mapping(source = "competition.competitionDate", target = "date")
    @Mapping(source = "competition.maxParticipants", target = "maxParticipants")
    @Mapping(source = "competition.competitionType", target = "competitionType")
    @Mapping(source = "organization", target = "organization")
    @Mapping(source = "competition.events", target = "eventEntities")
    CompetitionEntity toCompetitionEntity(OrganizationEntity organization, CreateCompetitionDto competition);

    /**
     * Получить информацию о соревновании.
     *
     * @param competition сущность соревнования
     * @return Информация о соревновании
     */
    CompetitionInfoDto mapToCompetitionInfoDto(CompetitionEntity competition);

    /**
     * Получить информацию о соревнованиях.
     *
     * @param competition сущности соревнований
     * @return Информация о соревнованиях
     */
    @Named(value = "mapCompetitions")
    List<CompetitionInfoDto> mapToCompetitionInfoDto(Set<CompetitionEntity> competition);

    /**
     * Получить полную информацию о соревновании
     *
     * @param competitionEntity сущность соревнования
     * @return полная информация о соревновании
     */
    @Mapping(source = "eventEntities", target = "events")
    @Mapping(source = "organization", target = "organizationInfo")
    CompetitionFullInfoDto mapToCompetitionFullInfo(CompetitionEntity competitionEntity);
}
