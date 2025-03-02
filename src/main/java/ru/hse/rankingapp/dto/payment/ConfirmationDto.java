package ru.hse.rankingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто с редиректом на оплату.
 */
@Data
@Accessors(chain = true)
public class ConfirmationDto {

    /**
     * Тип.
     */
    private String type;

    /**
     * Адрес для оплаты.
     */
    @JsonProperty("confirmation_url")
    private String confirmationUrl;
}