package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.password.PasswordChangeDto;
import ru.hse.rankingapp.dto.password.PasswordValidateTokenDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.service.PasswordService;

/**
 * API для изменения/восстановления пароля.
 */
@Tag(name = "Password", description = "API для изменения/восстановления пароля")
@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    /**
     * Изменить пароль.
     *
     * @param passwordChangeDto дто для изменения пароля
     */
    @PostMapping("/change")
    @Operation(summary = "Изменить пароль на новый")
    public void passwordChange(@RequestBody PasswordChangeDto passwordChangeDto) {
        passwordService.changePassword(passwordChangeDto);
    }

    /**
     * Отправить письмо с восстановлением пароля.
     *
     * @param email почта для отправления
     */
    @PostMapping("/recovery")
    @Operation(summary = "Отправить письмо на почту для изменения пароля")
    public void passwordRecovery(@RequestBody EmailRequestDto email) {
        passwordService.recoveryPassword(email);
    }

    /**
     * Провалидировать токен на смену пароля.
     */
    @PostMapping("/validate-token")
    @Operation(summary = "Валидация токена на смену пароля")
    public void validatePasswordToken(@RequestBody PasswordValidateTokenDto passwordValidateTokenDto) {
        passwordService.validateToken(passwordValidateTokenDto);
    }
}
