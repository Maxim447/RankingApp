package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.news.NewsCreateDto;
import ru.hse.rankingapp.dto.news.NewsResponseDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.entity.NewsEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.NewsMapper;
import ru.hse.rankingapp.repository.NewsRepository;
import ru.hse.rankingapp.service.search.NewsSearchWithSpec;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Сервис для работы с новостями.
 */
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsMapper newsMapper;
    private final NewsSearchWithSpec newsSearchWithSpec;
    private final NewsRepository newsRepository;
    private final FileService fileService;

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

    /**
     * Удалить новость по id.
     *
     * @param id Ид записи
     */
    @Transactional
    public void deleteNews(Long id) {
        NewsEntity news = newsRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Не удалось найти новость по id = " + id, HttpStatus.NOT_FOUND));

        Stream.of(news.getImage1(), news.getImage2(), news.getImage3())
                .filter(Objects::nonNull)
                .forEach(fileService::deleteFile);

        newsRepository.deleteNewsById(id);
    }
}
