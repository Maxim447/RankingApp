package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.curator.CuratorUserCreateDto;
import ru.hse.rankingapp.dto.curator.EmailPasswordDto;
import ru.hse.rankingapp.service.OrganizationService;

import java.util.Set;

/**
 * API для куратора организации.
 */
@Tag(name = "Curator", description = "API для куратора организации")
@RestController
@RequestMapping("/api/v1/organization/curator")
@RequiredArgsConstructor
public class CuratorController {

    private final OrganizationService organizationService;

    /**
     * Добавить пользователей к организации без приглашения.
     */
    @PostMapping("/add-users")
    @Operation(summary = "Добавить пользователей к организации без приглашения")
    public void addUsersToOrganizationWithoutConfirmation(@RequestBody Set<String> usersEmails) {
        organizationService.addUsersToOrganizationWithoutConfirmation(usersEmails);
    }

    /**
     * Создать аккаунт пользователя своей организации.
     */
    @PostMapping("/create-user")
    @Operation(summary = "Создать аккаунт пользователя своей организации")
    public EmailPasswordDto createUser(@RequestBody @Valid CuratorUserCreateDto curatorUserCreateDto) {
        return organizationService.curatorCreateUser(curatorUserCreateDto);
    }
}
