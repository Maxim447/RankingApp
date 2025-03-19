package ru.hse.rankingapp.dto.sponsor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
@Schema(description = "Дто для обновления спонсора")
public class SponsorUpdateDto {

    @Schema(description = "Описание/наименование")
    private String description;

    @Schema(description = "Лого")
    private MultipartFile logo;

    @Schema(description = "Нужно ли обновить лого")
    private Boolean isNeedUpdateLogo;
}
