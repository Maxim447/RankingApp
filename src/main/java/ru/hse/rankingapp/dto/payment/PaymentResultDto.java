package ru.hse.rankingapp.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Дто с информацией о платеж")
public class PaymentResultDto {

    @Schema(description = "Id платежа")
    private String id;

    @Schema(description = "Статус платежа")
    private String status;

    @Schema(description = "Ссылка для платежа")
    private String redirectUrl;
}
