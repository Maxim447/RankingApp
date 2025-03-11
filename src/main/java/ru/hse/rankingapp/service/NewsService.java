package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.news.NewsCreateDto;
import ru.hse.rankingapp.dto.news.NewsResponseDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.entity.NewsEntity;
import ru.hse.rankingapp.mapper.NewsMapper;
import ru.hse.rankingapp.repository.NewsRepository;
import ru.hse.rankingapp.service.search.NewsSearchWithSpec;

/**
 * Сервис для работы с новостями.
 */
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsMapper newsMapper;
    private final NewsSearchWithSpec newsSearchWithSpec;
    private final NewsRepository newsRepository;

    /**
     * Создать новость.
     *
     * @param newsCreateDto Дто для создания новости
     */
    public void createNews(NewsCreateDto newsCreateDto) {
        NewsEntity newsEntity = newsMapper.mapToNewsEntity(newsCreateDto);
        newsRepository.save(newsEntity);
    }

    /**
     * Получить список новостей.
     */
    public PageResponseDto<NewsResponseDto> getNews(PageRequestDto pageRequestDto) {
        Specification<NewsEntity> specification = newsSearchWithSpec.searchWithSpec();

        Page<NewsResponseDto> page = newsRepository.findAll(specification, pageRequestDto.toPageRequest())
                .map(newsMapper::mapToNewsResponse);

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }
}
