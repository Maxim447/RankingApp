package ru.hse.rankingapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурационный класс для работы с почтой.
 */
@Data
@Component
@ConfigurationProperties(value = "payment")
public class PaymentProperties {

    /**
     * API ключ.
     */
    private String key;

    /**
     * Id магазина.
     */
    private String id;
}
