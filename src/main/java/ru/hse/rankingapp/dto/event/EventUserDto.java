package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Информация об участниках заплыва.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Информация об участниках заплыва")
public class EventUserDto {

    @Schema(description = "Имя участника")
    private String firstName;

    @Schema(description = "Фамилия участника")
    private String lastName;

    @Schema(description = "Отчество участника")
    private String middleName;

    @Schema(description = "Дата рождения участника")
    private LocalDate birthDate;

    @Schema(description = "Пол участника")
    private Gender gender;

    @Schema(description = "Почта участника")
    private String email;

    @Schema(description = "Телефон участника")
    private String phone;

    @Schema(description = "Телефон на чрезвычайной ситуации")
    private String emergencyPhone;

    @Schema(description = "Время заплыва")
    private LocalTime time;

    @Schema(description = "Очки за заплыв")
    private Double points;

    @Schema(description = "Место в заплыве")
    private Integer place;

    @Schema(description = "Дата регистрации на заплыв")
    private LocalDate registrationDate;
}
