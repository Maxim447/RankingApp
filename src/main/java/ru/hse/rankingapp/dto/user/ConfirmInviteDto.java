package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто для принятия приглашения.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для принятия приглашения")
public class ConfirmInviteDto {

    @Schema(description = "Почта пользователя")
    private String userEmail;

    @Schema(description = "Почта организации")
    private String organizationEmail;
}
