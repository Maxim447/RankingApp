package ru.hse.rankingapp.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Дто с информацией о платеже для виджета")
public class PaymentWidgetResponseDto {

    @Schema(description = "Id платежа")
    private String id;

    @Schema(description = "Статус платежа")
    private String status;

    @Schema(description = "Токен для виджета")
    private String token;
}
