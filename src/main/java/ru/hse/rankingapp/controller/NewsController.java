package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.news.NewsResponseDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.service.NewsService;

/**
 * API для новостей.
 */
@Tag(name = "News", description = "API для новостей")
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * Получить список новостей.
     */
    @GetMapping
    @Operation(summary = "Получить список новостей")
    public PageResponseDto<NewsResponseDto> getNews(PageRequestDto pageRequestDto) {
        return newsService.getNews(pageRequestDto);
    }
}
