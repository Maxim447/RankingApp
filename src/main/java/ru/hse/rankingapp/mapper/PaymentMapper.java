package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.rankingapp.dto.payment.PaymentCreateDto;
import ru.hse.rankingapp.dto.payment.PaymentRequestDto;

/**
 * Маппер для платежей.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {


    /**
     * Маппинг из {@link PaymentCreateDto} в {@link PaymentRequestDto}.
     */
    @Mapping(source = "paymentSum", target = "amount.value")
    @Mapping(target = "amount.currency", constant = "RUB")
    @Mapping(target = "confirmation.type", constant = "redirect")
    @Mapping(source = "redirectUrl", target = "confirmation.returnUrl")
    @Mapping(target = "capture", constant = "true")
    PaymentRequestDto toPaymentRequestDto(PaymentCreateDto paymentCreateDto);
}
