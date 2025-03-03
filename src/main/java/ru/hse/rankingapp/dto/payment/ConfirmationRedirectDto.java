package ru.hse.rankingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто с редиректом.
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmationRedirectDto {

    /**
     * Тип.
     */
    private String type;

    /**
     * Адрес для возвращения после оплаты.
     */
    @JsonProperty("return_url")
    private String returnUrl;
}
