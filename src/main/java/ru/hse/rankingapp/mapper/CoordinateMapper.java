package ru.hse.rankingapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonDto;
import ru.hse.rankingapp.entity.CoordinateEntity;

/**
 * Маппер для координат.
 */
@Mapper(componentModel = "spring")
public interface CoordinateMapper {

    /**
     * Обновить местоположение.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "geometry", ignore = true)
    @Mapping(target = "organization", ignore = true)
    void updateCoordinate(SimpleGeoJsonDto simpleGeoJsonDto, @MappingTarget CoordinateEntity coordinateEntity);
}
