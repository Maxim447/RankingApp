package ru.hse.rankingapp.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Дто с информацией об уведомлении.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Дто с информацией об уведомлении")
public class NotificationResponse {

    @Schema(description = "ИД уведомления")
    private Long id;

    @Schema(description = "Текст уведомления")
    private String text;
}
