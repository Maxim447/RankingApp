package ru.hse.authservice.controller;

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
import ru.hse.authservice.dto.UserInfoDto;
import ru.hse.authservice.dto.user.UpdateEmailRequestDto;
import ru.hse.authservice.dto.user.UpdatePasswordRequestDto;
import ru.hse.authservice.dto.user.UpdatePhoneRequestDto;
import ru.hse.authservice.entity.User;
import ru.hse.authservice.service.UserService;

/**
 * API для пользователей.
 */
@Tag(name = "User", description = "API для пользователей")
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
    public UserInfoDto getAuthenticatedUser(@AuthenticationPrincipal User user) {
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
    public void updatePhone(@RequestBody @Valid UpdatePhoneRequestDto updatePhoneRequestDto, @AuthenticationPrincipal User user) {
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
    public void updateEmail(@RequestBody @Valid UpdateEmailRequestDto updateEmailRequestDto, @AuthenticationPrincipal User user) {
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
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto, @AuthenticationPrincipal User user) {
        userService.updatePassword(updatePasswordRequestDto, user);
    }
}
