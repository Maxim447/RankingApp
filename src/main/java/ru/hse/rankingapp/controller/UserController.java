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
import org.springframework.web.servlet.view.RedirectView;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.ConfirmInviteDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.service.UserService;

/**
 * API для пользователей.
 */
@Tag(name = "UserEntity", description = "API для пользователей")
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @param user авторизированный пользователь
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/info")
    @Operation(description = "Получить данные об авторизированном пользователе")
    public UserInfoDto getAuthenticatedUser(@AuthenticationPrincipal UserEntity user) {
        return userService.getAuthenticatedUser(user);
    }

    /**
     * Изменить номер телефона.
     *
     * @param updatePhoneRequestDto dto для изменения номера телефона
     * @param user                  авторизированный пользователь
     */
    @PostMapping("/update-phone")
    @Operation(description = "Изменить номер телефона")
    public void updatePhone(@RequestBody @Valid UpdatePhoneRequestDto updatePhoneRequestDto, @AuthenticationPrincipal UserEntity user) {
        userService.updatePhone(updatePhoneRequestDto, user);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     * @param user                  авторизированный пользователь
     */
    @PostMapping("/update-email")
    @Operation(description = "Изменить электронную почту")
    public void updateEmail(@RequestBody @Valid UpdateEmailRequestDto updateEmailRequestDto, @AuthenticationPrincipal UserEntity user) {
        userService.updateEmail(updateEmailRequestDto, user);
    }

    /**
     * Изменить пароль.
     *
     * @param updatePasswordRequestDto dto для изменения пароля
     * @param user                     авторизированный пользователь
     */
    @PostMapping("/update-password")
    @Operation(description = "Изменить пароль")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto, @AuthenticationPrincipal UserEntity user) {
        userService.updatePassword(updatePasswordRequestDto, user);
    }

    /**
     * Получить пользователей по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest пагинация
     * @return пагинированный ответ
     */
    @GetMapping("/search")
    @Operation(description = "Найти пользователей по параметрам поиска")
    public PageResponseDto<UserInfoDto> searchUsers(UserSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return userService.searchUsers(searchParams, pageRequest);
    }

    /**
     * Принять приглашение на вступление к организации.
     *
     * @param inviteDto информация о пользователе и организации.
     */
    @GetMapping("/confirm-invite")
    @Operation(description = "Принять запрос на вступление в организцию")
    public RedirectView confirmInvite(ConfirmInviteDto inviteDto) {
        userService.confirmInviteIntoOrganization(inviteDto);
        //todo написать редирект на главную страницу
        return new RedirectView("/confirmation-success");
    }
}
