package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalDate;

/**
 * Ответ на получение данных об авторизированном пользователе.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ на получение данных об авторизированном пользователе")
public class UserInfoDto {

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Телефон пользователя")
    private String phone;

    @Schema(description = "Телефон по которому можно позвонить в случае чего")
    private String emergencyPhone;

    @Schema(description = "Дата рождения")
    private LocalDate birthDate;

    @Schema(description = "Пол (MALE/FEMALE)")
    private Gender gender;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Рейтинг участника")
    private Double rating;
}
