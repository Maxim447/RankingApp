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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse.rankingapp.dto.login.LoginResponseDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserFullInfoDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.dto.user.rating.RatingSearchParamsDto;
import ru.hse.rankingapp.dto.user.rating.UserRatingDto;
import ru.hse.rankingapp.service.UserService;

import java.util.UUID;

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
     * @return dto c данными об авторизованном пользователе
     */
    @GetMapping(value = "/info")
    @Operation(summary = "Получить данные об авторизированном пользователе")
    public UserInfoDto getAuthenticatedUser() {
        return userService.getAuthenticatedUser();
    }

    /**
     * Изменить номер телефона.
     *
     * @param updatePhoneRequestDto dto для изменения номера телефона
     */
    @PostMapping("/update-phone")
    @Operation(summary = "Изменить номер телефона")
    public void updatePhone(@RequestBody @Valid UpdatePhoneRequestDto updatePhoneRequestDto) {
        userService.updatePhone(updatePhoneRequestDto);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     */
    @PostMapping("/update-email")
    @Operation(summary = "Изменить электронную почту")
    public LoginResponseDto updateEmail(@RequestBody @Valid EmailRequestDto updateEmailRequestDto) {
        return userService.updateEmail(updateEmailRequestDto);
    }

    /**
     * Получить пользователей по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return пагинированный ответ
     */
    @GetMapping("/search")
    @Operation(summary = "Найти пользователей по параметрам поиска")
    public PageResponseDto<UserInfoDto> searchUsers(UserSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return userService.searchUsers(searchParams, pageRequest);
    }

    /**
     * Добавить пользователя к заплыву.
     *
     * @param eventUuid Юид заплыва
     */
    @PostMapping("/add-to-event/{uuid}")
    @Operation(summary = "Записаться на заплыв")
    public void addToEvent(@PathVariable(value = "uuid") UUID eventUuid) {
        userService.addToEvent(eventUuid);
    }

    /**
     * Принять приглашение на вступление к организации.
     *
     * @param token токен на добавление
     */
    @GetMapping("/confirm-invite")
    @Operation(summary = "Принять запрос на вступление в организцию")
    public RedirectView confirmInvite(UUID token) {
        return userService.confirmInviteIntoOrganization(token);
    }

    /**
     * Получить полную информацию о пользователе.
     *
     * @return Полная информация о пользователе
     */
    @GetMapping("/full-info")
    @Operation(summary = "Получить полную информацию о пользователе")
    public UserFullInfoDto getUserFullInfoDto() {
        return userService.getUserFullInfo();
    }

    /**
     * Получить данные для таблицы с общим рейтингом.
     *
     * @param searchParams Поисковые параметры
     * @param pageRequest  пагинация
     * @return данные для таблицы с общим рейтингом
     */
    @GetMapping("/rating-search")
    @Operation(summary = "Получить данные для таблицы с общим рейтингом")
    public PageResponseDto<UserRatingDto> searchUsersRating(RatingSearchParamsDto searchParams, PageRequestDto pageRequest) {
        return userService.searchUsersRating(searchParams, pageRequest);
    }
}
