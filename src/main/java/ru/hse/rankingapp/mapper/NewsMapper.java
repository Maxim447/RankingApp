package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.news.NewsCreateDto;
import ru.hse.rankingapp.dto.news.NewsResponseDto;
import ru.hse.rankingapp.entity.NewsEntity;
import ru.hse.rankingapp.service.FileService;

/**
 * Маппер новостей.
 */
@Mapper(componentModel = "spring", uses = FileService.class)
public interface NewsMapper {

    /**
     * Маппинг {@link NewsCreateDto} в {@link NewsEntity}.
     */
    @Mapping(source = "image1", target = "image1", qualifiedByName = "saveFile")
    @Mapping(source = "image2", target = "image2", qualifiedByName = "saveFile")
    @Mapping(source = "image3", target = "image3", qualifiedByName = "saveFile")
    NewsEntity mapToNewsEntity(NewsCreateDto newsCreateDto);

    /**
     * Маппинг {@link NewsEntity} в {@link NewsResponseDto}.
     */
    NewsResponseDto mapToNewsResponse(NewsEntity newsEntity);
}
