package ru.hse.rankingapp.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто для обновления статуса организации.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для обновления статуса организации")
public class UpdateIsOpenStatusDto {

    @NotNull(message = "Статус открытости не может быть null")
    @Schema(description = "Статус организации (открытая/закрытая)")
    private Boolean isOpen;
}
