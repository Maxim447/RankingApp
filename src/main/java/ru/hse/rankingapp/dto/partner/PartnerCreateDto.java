package ru.hse.rankingapp.dto.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания партнера")
public class PartnerCreateDto {

    @Schema(description = "Описание/наименование")
    @NotNull(message = "Описание/наименование обязательно к заполнению")
    private String description;

    @Schema(description = "Фото")
    @NotNull(message = "Фото обязательно к заполнению")
    private MultipartFile logo;
}
