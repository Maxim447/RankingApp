package ru.hse.rankingapp.dto.payment;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Создать запрос на платеж.
 */
@Data
@Accessors(chain = true)
public class PaymentRequestDto {

    /**
     * Дто с суммой для оплаты.
     */
    private AmountDto amount;

    /**
     * Дто с редиректом.
     */
    private ConfirmationRedirectDto confirmation;

    /**
     * Спишутся ли деньги сразу или заморозятся на счете.
     */
    private boolean capture;

    /**
     * Описание платежа.
     */
    private String description;
}
