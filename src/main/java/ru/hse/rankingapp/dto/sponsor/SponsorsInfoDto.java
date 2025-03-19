package ru.hse.rankingapp.dto.sponsor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о спонсоре")
public class SponsorsInfoDto {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Лого спонсора")
    private String sponsorLogo;

    @Schema(description = "Описание спонсора")
    private String sponsorDescription;
}
