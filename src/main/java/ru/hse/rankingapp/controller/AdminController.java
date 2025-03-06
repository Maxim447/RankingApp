package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonDto;
import ru.hse.rankingapp.service.CoordinateService;

/**
 * API для админа.
 */
@Tag(name = "Admin", description = "API для админа")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CoordinateService coordinateService;

    /**
     * Сохранить местоположение организации.
     *
     * @param simpleGeoJsonDto информация о координатах
     */
    @PostMapping("/coordinates")
    public void addCoordinates(@RequestBody SimpleGeoJsonDto simpleGeoJsonDto) {
        coordinateService.addCoordinates(simpleGeoJsonDto);
    }
}
