package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonDto;
import ru.hse.rankingapp.dto.news.NewsCreateDto;
import ru.hse.rankingapp.service.CoordinateService;
import ru.hse.rankingapp.service.NewsService;

/**
 * API для админа.
 */
@Tag(name = "Admin", description = "API для админа")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CoordinateService coordinateService;
    private final NewsService newsService;

    /**
     * Сохранить местоположение организации.
     *
     * @param simpleGeoJsonDto информация о координатах
     */
    @PostMapping("/coordinates")
    @Operation(summary = "Добавить координаты")
    public void addCoordinates(@RequestBody SimpleGeoJsonDto simpleGeoJsonDto) {
        coordinateService.addCoordinates(simpleGeoJsonDto);
    }

    /**
     * Удалить координаты по id.
     *
     * @param id Id в таблице координат
     */
    @DeleteMapping("/coordinates/{id}")
    @Operation(summary = "Удалить координаты")
    public void deleteCoordinates(@PathVariable(value = "id") Long id) {
        coordinateService.deleteById(id);
    }

    /**
     * Создать новость.
     *
     * @param newsCreateDto Дто для создания новости
     */
    @PostMapping(value = "/news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создать новость")
    public void createNews(@ModelAttribute @Valid NewsCreateDto newsCreateDto) {
        newsService.createNews(newsCreateDto);
    }
}
