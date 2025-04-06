package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalTime;

/**
 * Дто для загрузки информации о заплыве.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для загрузки информации о заплыве")
public class EventResultRequestDto {

    @Schema(description = "Почта пользователя")
    @NotNull(message = "Почта обязательна к заполнению")
    private String userEmail;

    @Schema(description = "Дистанция заплыва")
    @NotNull(message = "Дистанция должна быть заполнена")
    private Integer distance;

    @Schema(description = "Пол участника")
    @NotNull(message = "Гендер обязателен к заполнению")
    private Gender gender;

    @Schema(description = "Время пользователя")
    @NotNull(message = "Время участника обязательно к заполнению")
    private LocalTime userTime;
}
