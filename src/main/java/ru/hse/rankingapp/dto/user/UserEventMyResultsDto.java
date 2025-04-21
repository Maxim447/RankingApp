package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

/**
 * Дто для отображения заплывов в карточке "Мои результаты".
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для отображения заплывов в карточке \"Мои результаты\"")
public class UserEventMyResultsDto {

    @Schema(description = "Пол")
    private String gender;

    @Schema(description = "Возраст от")
    private Integer ageFrom;

    @Schema(description = "Возраст до")
    private Integer ageTo;

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль заплыва")
    private String style;

    @Schema(description = "Время за которое проплыта дистанция")
    private LocalTime time;

    @Schema(description = "Занятое место")
    private Integer place;
}
