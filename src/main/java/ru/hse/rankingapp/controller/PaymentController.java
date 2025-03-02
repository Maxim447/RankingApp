package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.payment.PaymentCreateDto;
import ru.hse.rankingapp.dto.payment.PaymentInfoDto;
import ru.hse.rankingapp.dto.payment.PaymentResultDto;
import ru.hse.rankingapp.service.PaymentService;

/**
 * API для платежей.
 */
@Tag(name = "Payment", description = "API для платежей")
@RequestMapping("/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Создать платеж.
     */
    @PostMapping
    @Operation(summary = "Создать платеж")
    public PaymentResultDto createPayment(@RequestBody @Valid PaymentCreateDto paymentCreateDto) {
        return paymentService.createPayment(paymentCreateDto);
    }

    /**
     * Получить информацию о платеже.
     */
    @GetMapping("/{paymentId}")
    @Operation(summary = "Получить информацию о платеже")
    public PaymentInfoDto getPaymentInfo(@PathVariable(value = "paymentId") String paymentId) {
        return paymentService.getPaymentInfo(paymentId);
    }
}
