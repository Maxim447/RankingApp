package ru.hse.rankingapp.dto.payment;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто с суммой для оплаты.
 */
@Data
@Accessors(chain = true)
public class AmountDto {

    /**
     * Сумма.
     */
    private String value;

    /**
     * Валюта.
     */
    private String currency;
}
