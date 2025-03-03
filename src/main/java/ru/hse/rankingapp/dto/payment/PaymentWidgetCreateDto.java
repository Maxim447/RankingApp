package ru.hse.rankingapp.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentWidgetCreateDto {

    @Schema(description = "Сумма для оплаты")
    @NotNull
    private Double paymentSum;

    @Schema(description = "Описание товара/услуги")
    @NotNull
    private String description;
}
