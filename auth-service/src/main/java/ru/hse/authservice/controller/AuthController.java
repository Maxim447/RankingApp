package ru.hse.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.authservice.dto.LoginRequestDto;
import ru.hse.authservice.dto.LoginResponseDto;
import ru.hse.authservice.dto.SignUpRequestDto;
import ru.hse.authservice.dto.VerificationCodeResponseDto;
import ru.hse.authservice.service.AuthService;

/**
 * API для аутентификации.
 */
@Tag(name = "Authentication", description = "API для аутентификации")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Получить код для подтверждения электронной почты.
     *
     * @param emailToVerify почта для подтверждения
     * @return dto с кодом подтверждения
     */
    @GetMapping("/verify-email")
    @Operation(summary = "Получить код для подтверждения электронной почты")
    public VerificationCodeResponseDto getEmailVerificationCode(@RequestParam("emailToVerify") String emailToVerify) {
        return authService.getEmailVerificationCode(emailToVerify);
    }

    /**
     * Зарегистрировать пользователя.
     *
     * @param signUpRequestDto Дто запроса для регистрации
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация")
    public void signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
    }

    /**
     * Произвести login пользователя.
     *
     * @param loginRequestDto Дто запрос для входа
     * @return Дто ответа для входа
     */
    @PostMapping("/login")
    @Operation(summary = "Вход")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
}
