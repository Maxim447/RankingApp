package ru.hse.rankingapp.dto.user.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

/**
 * Дто с рейтингом спортсмена.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с рейтингом спортсмена")
public class UserRatingDto {

    @Schema(description = "Id пользователя")
    private Long id;

    @Schema(description = "ФИО пользователя")
    private String fullName;

    @Schema(description = "Пол пользователя")
    private String gender;

    @Schema(description = "Возраст пользователя")
    private Integer age;

    @Schema(description = "Рейтинг пользователя")
    private Double rating;

    @Schema(description = "Кол-во стартов пользователя")
    private Long starts;

    @Schema(description = "Кол-во первых мест пользователя")
    private Long firstPlaceCount;

    @Schema(description = "Кол-во вторых мест пользователя")
    private Long secondPlaceCount;

    @Schema(description = "Кол-во третьих мест пользователя")
    private Long thirdPlaceCount;

    @Schema(description = "Лучший темп на 100 метров пользователя")
    private LocalTime bestTime100;

    @Schema(description = "Любитель/Профессионал")
    private String participantType;
}
