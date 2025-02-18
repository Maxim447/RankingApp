package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.dto.event.EventInfoDto;
import ru.hse.rankingapp.dto.event.EventUserDto;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.enums.StatusEnum;

import java.util.List;
import java.util.Set;

/**
 * Маппер для работы с сущностью мероприятий.
 */
@Mapper(componentModel = "spring", imports = StatusEnum.class)
public interface EventMapper {


    /**
     * {@link CreateEventDto} to {@link EventEntity}.
     *
     * @param eventDto дто для создания сущности
     * @return {@link EventEntity}
     */
    @Mapping(target = "status", expression = "java(StatusEnum.CREATED)")
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

    /**
     * Смапить список информаций о заплывах.
     *
     * @param eventUserLinkEntities Линковочная таблица пользователя и заплыва
     * @return Информация о заплывах
     */
    List<EventInfoDto> mapEventInfoDtoList(Set<EventUserLinkEntity> eventUserLinkEntities);

    /**
     * Смапить информацию о заплыве.
     *
     * @param eventUserLinkEntities Линковочная таблица пользователя и заплыва
     * @return Информация о заплыве
     */
    @Mapping(source = "event", target = ".")
    EventInfoDto mapEventInfoDto(EventUserLinkEntity eventUserLinkEntities);
}
