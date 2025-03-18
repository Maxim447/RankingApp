package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Дто для создания мероприятия.
 */
@Data
@Accessors(chain = true)
public class CreateEventDto {

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль")
    private String style;

    @Schema(description = "Гендер участников")
    private Gender gender;

    @Schema(description = "Возрастная категория с")
    private Integer ageFrom;

    @Schema(description = "Возрастная категория по")
    private Integer ageTo;

    @Schema(description = "Стоимость участия", defaultValue = "0.0")
    private Double price;

    @Schema(description = "Максимальное число участников")
    private Integer maxParticipants;

    @Schema(description = "Ссылка на трансляцию (Например, ВК видео)")
    private String videoLink;

    @Schema(description = "Время проведения события")
    private OffsetDateTime startTime;

    @Schema(description = "Прикрепление к определенному соревнованию")
    private UUID competitionUUID;
}
