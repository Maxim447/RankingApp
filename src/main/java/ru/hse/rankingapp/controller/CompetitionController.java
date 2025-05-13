package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.dto.competition.CompetitionFullInfoDto;
import ru.hse.rankingapp.dto.competition.CompetitionInfoDto;
import ru.hse.rankingapp.dto.competition.CompetitionSearchParamsDto;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.service.CompetitionService;

import java.util.UUID;

/**
 * API для соревнований.
 */
@Tag(name = "Competition", description = "API для соревнований")
@RestController
@RequestMapping("/api/v1/competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    /**
     * Создать соревнование.
     *
     * @param createCompetitionDto Дто для создания соревнования
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создать соревнование")
    public void createCompetition(@RequestPart(value = "attachment") MultipartFile attachment,
            @RequestPart(value = "competition") CreateCompetitionDto createCompetitionDto) {
        competitionService.createCompetition(createCompetitionDto, attachment);
    }

    /**
     * Найти соревнование по его uuid.
     *
     * @param uuid юид соревнования
     * @return Полная информация о соревновании
     */
    @GetMapping("/find/{uuid}")
    @Operation(summary = "Найти полную информацию о соревновании по его uuid")
    public CompetitionFullInfoDto getCompetitionFullInfoByUuid(@PathVariable UUID uuid) {
        return competitionService.getCompetitionFullInfoByUuid(uuid);
    }

    /**
     * Найти соревнования по поисковым параметрам.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest пагинация
     * @return соревнования
     */
    @GetMapping("/search")
    @Operation(summary = "Найти соревнования по поисковым параметрам")
    public PageResponseDto<CompetitionInfoDto> searchCompetitions(CompetitionSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return competitionService.searchCompetitions(searchParams, pageRequest);
    }

    /**
     * Удалить соревнование по uuid.
     *
     * @param competitionUuid Uuid соревнования
     */
    @DeleteMapping("/delete/{uuid}")
    @Operation(summary = "Удалить соревнование по uuid")
    public void deleteCompetitionByUuid(@PathVariable(value = "uuid") UUID competitionUuid) {
        competitionService.deleteCompetition(competitionUuid);
    }
}
