package ru.hse.rankingapp.dto.aboutus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@Schema(description = "Текст о нас")
public class AboutUsInfoDto {

    @Schema(description = "Описание о нас")
    private String description;
}
