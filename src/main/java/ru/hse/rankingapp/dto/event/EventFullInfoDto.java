package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.entity.enums.StatusEnum;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Дто с полной информацией о заплыве.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с полной информацией о заплыве")
public class EventFullInfoDto {

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль плавания")
    private String style;

    @Schema(description = "Пол участников")
    private Gender gender;

    @Schema(description = "Возрастная категория с")
    private Integer ageFrom;

    @Schema(description = "Возрастная категория по")
    private Integer ageTo;

    @Schema(description = "Максимальное число участников")
    private Integer maxParticipants;

    @Schema(description = "Ссылка на трансляцию")
    private String videoLink;

    @Schema(description = "Стоимость заплыва")
    private Double price;

    @Schema(description = "Максимальное кол-во очков за заплыв")
    private Integer maxPoints;

    @Schema(description = "Время начала заплыва")
    private OffsetDateTime startTime;

    @Schema(description = "Статус")
    private StatusEnum status;

    @Schema(description = "Uuid заплыва")
    private UUID eventUuid;

    @Schema(description = "Информация об участниках")
    private List<EventUserDto> users;
}
