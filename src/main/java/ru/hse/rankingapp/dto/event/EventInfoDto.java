package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Дто с информацией о заплыве.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с информацией о заплыве")
public class EventInfoDto {

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль плавания")
    private String style;

    @Schema(description = "Пол участников")
    private Gender gender;

    @Schema(description = "Возрастная группа участников")
    private String ageCategory;

    @Schema(description = "Максимальное кол-во очков за заплыв")
    private Integer maxPoints;

    @Schema(description = "Время начала заплыва")
    private LocalDate startTime;

    @Schema(description = "Время конца заплыва")
    private LocalDate endTime;

    @Schema(description = "Uuid заплыва")
    private UUID eventUuid;
}
