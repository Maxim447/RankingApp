package ru.hse.rankingapp.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто для создания платежа.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания платежа")
public class PaymentCreateDto {

    @Schema(description = "Сумма для оплаты")
    @NotNull
    private Double paymentSum;

    @Schema(description = "Путь для редиректа после успешного/неуспешного платежа")
    @NotNull
    private String redirectUrl;

    @Schema(description = "Описание товара/услуги")
    @NotNull
    private String description;
}
