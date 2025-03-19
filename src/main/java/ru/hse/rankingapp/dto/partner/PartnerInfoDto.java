package ru.hse.rankingapp.dto.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о партере")
public class PartnerInfoDto {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Лого партнера")
    private String partnerLogo;

    @Schema(description = "Описание партнера")
    private String partnerDescription;
}
