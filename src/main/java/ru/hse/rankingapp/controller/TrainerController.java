package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.trainer.TrainerInfoDto;
import ru.hse.rankingapp.service.TrainerService;

/**
 * API для тренеров.
 */
@Tag(name = "Trainers", description = "API для тренеров")
@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    /**
     * Получить тренеров по id местоположения.
     */
    @GetMapping("/{coordinateId}")
    @Operation(summary = "Получить тренеров по id местоположения")
    public PageResponseDto<TrainerInfoDto> getTrainersByCoordinateId(@PathVariable(name = "coordinateId") Long coordinateId, PageRequestDto pageRequestDto) {
        return trainerService.getTrainersByCoordinateId(coordinateId, pageRequestDto);
    }
}
