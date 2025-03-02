package ru.hse.rankingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/**
 * Информация о платеже.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Информация о платеже")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfoDto {

    @Schema(description = "Ид платежа")
    private String id;

    @Schema(description = "Статус платежа")
    private String status;

    @Schema(description = "Оплатил ли пользователь")
    private boolean paid;

    @Schema(description = "Сумма платежа")
    private AmountDto amount;

    @Schema(description = "Описание платежа")
    private String description;

    @Schema(description = "Дата создания платежа")
    @JsonProperty(value = "created_at")
    private OffsetDateTime createdAt;

    @Schema(description = "Дата истечения платежа")
    @JsonProperty(value = "expires_at")
    private OffsetDateTime expiresAt;
}
