package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
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
     * @param organization авторизированная организация
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/short-info")
    @Operation(description = "Получить краткую информацию об авторизированной организации")
    public OrganizationInfoDto getAuthenticatedUser(@AuthenticationPrincipal OrganizationEntity organization) {
        return organizationService.getAuthenticatedOrganization(organization);
    }

    /**
     * Получить полные данные об авторизированной организации.
     *
     * @param organization авторизированная организация
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/full-info")
    @Operation(description = "Получить полную информацию об авторизированной организации")
    public OrganizationFullInfoDto getOrganizationFullInfo(@AuthenticationPrincipal OrganizationEntity organization) {
        return organizationService.getOrganizationFullInfo(organization);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     * @param organization          авторизированная организация
     */
    @PostMapping("/update-email")
    @Operation(description = "Изменить электронную почту")
    public void updateEmail(@RequestBody @Valid UpdateEmailRequestDto updateEmailRequestDto, @AuthenticationPrincipal OrganizationEntity organization) {
        organizationService.updateEmail(updateEmailRequestDto, organization);
    }

    /**
     * Изменить пароль.
     *
     * @param updatePasswordRequestDto dto для изменения пароля
     * @param organization             авторизированная организация
     */
    @PostMapping("/update-password")
    @Operation(description = "Изменить пароль")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto, @AuthenticationPrincipal OrganizationEntity organization) {
        organizationService.updatePassword(updatePasswordRequestDto, organization);
    }

    /**
     * Получить организации по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return пагинированный ответ
     */
    @GetMapping("/search")
    @Operation(description = "Найти организации по параметрам поиска")
    public PageResponseDto<OrganizationInfoDto> searchOrganization(OrganizationSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return organizationService.searchOrganization(searchParams, pageRequest);
    }

    /**
     * Отправить приглашение пользователю(ям) на вступление в организацию.
     *
     * @param organization Сущность организации
     * @param usersEmail   почты пользователей
     */
    @PostMapping("/send-invite-to-users")
    @Operation(description = "Отправить приглашение пользователю(ям) на вступление в организацию")
    public void addUsersToOrganization(@AuthenticationPrincipal OrganizationEntity organization, @RequestBody Set<String> usersEmail) {
        organizationService.addUsersToOrganization(organization, usersEmail);
    }
}
