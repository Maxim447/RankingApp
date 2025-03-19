package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.aboutus.AboutUsInfoDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.partner.PartnerInfoDto;
import ru.hse.rankingapp.dto.sponsor.SponsorsInfoDto;
import ru.hse.rankingapp.service.AboutUsService;

/**
 * API для карточки о нас.
 */
@Tag(name = "AboutUs", description = "API для карточки о нас")
@RestController
@RequestMapping("/api/v1/about-us")
@RequiredArgsConstructor
public class AboutUsController {

    private final AboutUsService aboutUsService;

    /**
     * Получить текст о нас.
     */
    @GetMapping("/info")
    @Operation(summary = "Текст о нас")
    public AboutUsInfoDto getAboutUsInfo() {
        return aboutUsService.getAboutUsInfo();
    }

    /**
     * Информация о партнерах.
     */
    @GetMapping("/partners")
    @Operation(summary = "Информация о партнерах")
    public PageResponseDto<PartnerInfoDto> getPartners(PageRequestDto pageRequestDto) {
        return aboutUsService.getPartners(pageRequestDto);
    }

    /**
     * Информация о спонсорах.
     */
    @GetMapping("/sponsors")
    @Operation(summary = "Информация о спонсорах")
    public PageResponseDto<SponsorsInfoDto> getSponsors(PageRequestDto pageRequestDto) {
        return aboutUsService.getSponsors(pageRequestDto);
    }
}
