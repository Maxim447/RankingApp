package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.dto.event.EventInfoDto;
import ru.hse.rankingapp.dto.event.EventUserDto;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;

/**
 * Маппер для работы с сущностью мероприятий.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {


    /**
     * {@link CreateEventDto} to {@link EventEntity}.
     *
     * @param eventDto дто для создания сущности
     * @return {@link EventEntity}
     */
    EventEntity toEventEntity(CreateEventDto eventDto);

    /**
     * {@link EventEntity} to {@link EventInfoDto}.
     *
     * @param event cущность
     * @return {@link EventInfoDto}
     */
    EventInfoDto toEventInfoDto(EventEntity event);

    /**
     * {@link EventEntity} to {@link EventFullInfoDto}.
     *
     * @param event cущность
     * @return {@link EventFullInfoDto}
     */
    @Mapping(source = "eventUserLinks", target = "users")
    EventFullInfoDto toEventFullInfoDto(EventEntity event);

    /**
     * {@link EventUserLinkEntity} to {@link EventUserDto}.
     *
     * @param entity cущность линковычной таблицы заплыва и пользователя
     * @return {@link EventUserDto}
     */
    @Mapping(source = "user.", target = ".")
    EventUserDto mapEventUserDto(EventUserLinkEntity entity);
}
