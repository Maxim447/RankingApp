package ru.hse.rankingapp.dto.professional.records;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.deserializer.LocalTimeDeserializer;

import java.time.LocalTime;

/**
 * Дто для изменения времени рекорда.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для изменения времени рекорда")
public class UpdateRecordTimeDto {

    @Schema(description = "ИД записи")
    @NotNull(message = "Ид записи обязательно к заполнению")
    private Long id;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Schema(description = "Новое время")
    @NotNull(message = "Новое время рекорда обязательно к заполнению")
    private LocalTime newTime;
}
