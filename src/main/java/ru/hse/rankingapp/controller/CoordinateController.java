package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonResponseDto;
import ru.hse.rankingapp.service.CoordinateService;

import java.util.List;

/**
 * API для координат.
 */
@Tag(name = "Coordinates", description = "API для координат")
@RestController
@RequestMapping("/api/v1/coordinates")
@RequiredArgsConstructor
public class CoordinateController {

    private final CoordinateService coordinateService;

    @GetMapping
    @Operation(summary = "Получить все координаты")
    public List<SimpleGeoJsonResponseDto> getAllCoordinates() {
        return coordinateService.getAllCoordinates();
    }
}
