package ru.hse.rankingapp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.hse.rankingapp.config.FeignConfig;
import ru.hse.rankingapp.dto.payment.PaymentInfoDto;
import ru.hse.rankingapp.dto.payment.PaymentRequestDto;
import ru.hse.rankingapp.dto.payment.PaymentResponseDto;

@FeignClient(
        name = "PaymentClient",
        url = "${payment.url}",
        configuration = FeignConfig.class
)
public interface PaymentFeignClient {

    /**
     * Создать платеж.
     */
    @PostMapping(value = "/payments", consumes = "application/json")
    PaymentResponseDto createPayment(
            @RequestBody PaymentRequestDto paymentRequest,
            @RequestHeader("Idempotence-Key") String idempotenceKey
    );

    /**
     * Получить информацию о платеже.
     */
    @GetMapping(value = "/payments/{paymentId}", consumes = "application/json")
    PaymentInfoDto getPaymentInfo(@PathVariable(value = "paymentId") String paymentId);
}
