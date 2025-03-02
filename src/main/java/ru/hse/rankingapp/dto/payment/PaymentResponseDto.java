package ru.hse.rankingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/**
 * Дто со статусом платежа.
 */
@Data
@Accessors(chain = true)
public class PaymentResponseDto {

    /**
     * Id платежа.
     */
    private String id;

    /**
     * Статус платежа.
     */
    private String status;

    /**
     * Дто с суммой для оплаты платежа.
     */
    private AmountDto amount;

    /**
     * Описание платежа.
     */
    private String description;

    /**
     * Дто с получателем.
     */
    private RecipientDto recipient;

    /**
     * Дата создания платежа.
     */
    @JsonProperty(value = "created_at")
    private OffsetDateTime createdAt;

    /**
     * Дто с редиректом на оплату.
     */
    private ConfirmationDto confirmation;

    /**
     * Тестовый.
     */
    private boolean test;

    /**
     * Оплаченный.
     */
    private boolean paid;

    /**
     * Подлежащий возврату.
     */
    private boolean refundable;

    /**
     * Доп информация о платеже.
     */
    private Object metadata;
}
