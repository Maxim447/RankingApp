package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.service.CompetitionService;

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
     * @param organization организация
     * @param createCompetitionDto  Дто для создания соревнования
     */
    @PostMapping("/create")
    @Operation(summary = "Создать соревнование")
    public void createCompetition(@AuthenticationPrincipal OrganizationEntity organization, @RequestBody CreateCompetitionDto createCompetitionDto) {
        competitionService.createCompetition(organization, createCompetitionDto);
    }
}
