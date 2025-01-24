package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.entity.EventEntity;

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
}
