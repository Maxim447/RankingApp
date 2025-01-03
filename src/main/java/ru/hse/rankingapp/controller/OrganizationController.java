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
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.entity.Organization;
import ru.hse.rankingapp.service.OrganizationService;

/**
 * API для организаций.
 */
@Tag(name = "Organization", description = "API для организаций")
@RequestMapping("/api/v1/organization")
@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @param organization авторизированная организация
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/info")
    @Operation(description = "Получить данные об авторизированном пользователе")
    public OrganizationInfoDto getAuthenticatedUser(@AuthenticationPrincipal Organization organization) {
        return organizationService.getAuthenticatedOrganization(organization);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     * @param organization          авторизированная организация
     */
    @PostMapping("/update-email")
    @Operation(description = "Изменить электронную почту")
    public void updateEmail(@RequestBody @Valid UpdateEmailRequestDto updateEmailRequestDto, @AuthenticationPrincipal Organization organization) {
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
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto, @AuthenticationPrincipal Organization organization) {
        organizationService.updatePassword(updatePasswordRequestDto, organization);
    }
}
