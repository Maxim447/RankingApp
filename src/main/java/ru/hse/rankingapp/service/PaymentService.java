package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.payment.PaymentCreateDto;
import ru.hse.rankingapp.dto.payment.PaymentInfoDto;
import ru.hse.rankingapp.dto.payment.PaymentRequestDto;
import ru.hse.rankingapp.dto.payment.PaymentResponseDto;
import ru.hse.rankingapp.dto.payment.PaymentResultDto;
import ru.hse.rankingapp.feign.PaymentFeignClient;
import ru.hse.rankingapp.mapper.PaymentMapper;

import java.util.UUID;

/**
 * Сервис для совершения платежей.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentFeignClient paymentFeignClient;
    private final PaymentMapper paymentMapper;

    /**
     * Создать платеж.
     */
    public PaymentResultDto createPayment(PaymentCreateDto paymentCreateDto) {
        PaymentRequestDto paymentRequestDto = paymentMapper.toPaymentRequestDto(paymentCreateDto);

        String uniqueKey = UUID.randomUUID().toString();
        PaymentResponseDto payment = paymentFeignClient.createPayment(paymentRequestDto, uniqueKey);

        return new PaymentResultDto()
                .setId(payment.getId())
                .setStatus(payment.getStatus())
                .setRedirectUrl(payment.getConfirmation().getConfirmationUrl());
    }

    /**
     * Получить информацию о платеже.
     */
    public PaymentInfoDto getPaymentInfo(String paymentId) {
        return paymentFeignClient.getPaymentInfo(paymentId);
    }
}
