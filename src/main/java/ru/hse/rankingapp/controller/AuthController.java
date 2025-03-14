package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.login.LoginRequestDto;
import ru.hse.rankingapp.dto.login.LoginResponseDto;
import ru.hse.rankingapp.dto.organization.SignUpOrganizationRequestDto;
import ru.hse.rankingapp.dto.user.SignUpUserRequestDto;
import ru.hse.rankingapp.dto.VerificationCodeResponseDto;
import ru.hse.rankingapp.service.auth.AuthService;

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
     * Существует ли аккаунт.
     */
    @GetMapping("/exist-email/{email}")
    @Operation(summary = "Существует ли аккаунт с такой почтой")
    public boolean existAccount(@PathVariable(value = "email") String email) {
        return authService.existAccount(email);
    }

    /**
     * Зарегистрировать пользователя.
     *
     * @param signUpRequestDto Дто запроса для регистрации
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация пользователя")
    public void signUpUser(@RequestBody @Valid SignUpUserRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
    }

    /**
     * Зарегистрировать организацию.
     *
     * @param signUpRequestDto Дто запроса для регистрации
     */
    @PostMapping("/sign-up-organization")
    @Operation(summary = "Регистрация организации")
    public void signUpOrganization(@RequestBody @Valid SignUpOrganizationRequestDto signUpRequestDto) {
        authService.signUpOrganization(signUpRequestDto);
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

    /**
     * Получить Информацию о пользователе по токену.
     *
     * @param token Токен пользователя
     * @return Информация о пользователе по токену
     */
    @GetMapping("/info-by-token")
    @Operation(summary = "Получить информацию о пользователе по токену")
    public UserAuthentication getUserInfoByToken(@RequestParam(value = "token") String token) {
        return authService.getUserInfoByToken(token);
    }
}
