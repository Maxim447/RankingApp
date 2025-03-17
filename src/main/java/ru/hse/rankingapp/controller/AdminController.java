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
import ru.hse.rankingapp.dto.news.NewsUpdateDto;
import ru.hse.rankingapp.dto.trainer.TrainerCreateDto;
import ru.hse.rankingapp.service.CoordinateService;
import ru.hse.rankingapp.service.NewsService;
import ru.hse.rankingapp.service.OrganizationService;

/**
 * API для админа.
 */
@Tag(name = "Admin", description = "API для админа")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrganizationService organizationService;
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
     * Добавить тренеров к местоположению.
     */
    @PostMapping(value = "/add-trainer/{coordinateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить тренеров к местоположению")
    public void addTrainers(@PathVariable(name = "coordinateId") Long coordinateId, @ModelAttribute @Valid TrainerCreateDto trainerCreateDto) {
        coordinateService.addTrainers(coordinateId, trainerCreateDto);
    }

    /**
     * Обновить местоположение организации.
     *
     * @param simpleGeoJsonDto информация о координатах
     */
    @PostMapping("/coordinates-update/{id}")
    @Operation(summary = "Обновить координаты")
    public void updateCoordinates(@PathVariable(value = "id") Long id, @RequestBody SimpleGeoJsonDto simpleGeoJsonDto) {
        coordinateService.updateCoordinates(id, simpleGeoJsonDto);
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

    /**
     * Обновить новость.
     *
     * @param newsUpdateDto Дто для обновления новости
     */
    @PostMapping(value = "/news-update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновить новость")
    public void updateNews(@PathVariable(value = "id") Long id, @ModelAttribute NewsUpdateDto newsUpdateDto) {
        newsService.updateNews(id, newsUpdateDto);
    }

    /**
     * Удалить новость по id.
     *
     * @param id Ид записи
     */
    @DeleteMapping("/news/{id}")
    @Operation(summary = "Удалить новость")
    public void deleteNews(@PathVariable(value = "id") Long id) {
        newsService.deleteNews(id);
    }

    /**
     * Добавить роль куратора.
     *
     * @param email Email организации
     */
    @PostMapping("/curator/{email}")
    @Operation(summary = "Добавить роль куратора")
    public void addCurator(@PathVariable(value = "email") String email) {
        organizationService.addCurator(email);
    }
}
