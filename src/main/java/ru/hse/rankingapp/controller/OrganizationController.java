package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.dto.organization.UpdateIsOpenStatusDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.service.OrganizationService;

import java.util.Set;

/**
 * API для организаций.
 */
@Tag(name = "OrganizationEntity", description = "API для организаций")
@RequestMapping("/api/v1/organization")
@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * Получить данные об авторизированной организации.
     *
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/short-info")
    @Operation(summary = "Получить краткую информацию об авторизированной организации")
    public OrganizationInfoDto getAuthenticatedUser() {
        return organizationService.getAuthenticatedOrganization();
    }

    /**
     * Получить полные данные об авторизированной организации.
     *
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/full-info")
    @Operation(summary = "Получить полную информацию об авторизированной организации")
    public OrganizationFullInfoDto getOrganizationFullInfo() {
        return organizationService.getOrganizationFullInfo();
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     */
    @PostMapping("/update-email")
    @Operation(summary = "Изменить электронную почту")
    public void updateEmail(@RequestBody @Valid EmailRequestDto updateEmailRequestDto) {
        organizationService.updateEmail(updateEmailRequestDto);
    }

    /**
     * Изменить признак открытости у организации
     *
     * @param updateIsOpenStatusDto Дто для обновления статуса организации
     */
    @PostMapping("/update-open-status")
    @Operation(summary = "Изменить признак открытости у организации")
    public void updateOpenStatus(@RequestBody @Valid UpdateIsOpenStatusDto updateIsOpenStatusDto) {
        organizationService.updateOpenStatus(updateIsOpenStatusDto);
    }

    /**
     * Получить организации по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return пагинированный ответ
     */
    @GetMapping("/search")
    @Operation(summary = "Найти организации по параметрам поиска")
    public PageResponseDto<OrganizationInfoDto> searchOrganization(OrganizationSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return organizationService.searchOrganization(searchParams, pageRequest);
    }

    /**
     * Отправить приглашение пользователю(ям) на вступление в организацию.
     *
     * @param usersEmail почты пользователей
     */
    @PostMapping("/send-invite-to-users")
    @Operation(summary = "Отправить приглашение пользователю(ям) на вступление в организацию")
    public void addUsersToOrganization(@RequestBody Set<String> usersEmail) {
        organizationService.addUsersToOrganization(usersEmail);
    }
}
