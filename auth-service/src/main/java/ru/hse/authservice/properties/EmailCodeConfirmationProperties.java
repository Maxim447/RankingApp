package ru.hse.authservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурационный класс для работы с почтой.
 */
@Data
@Component
@ConfigurationProperties("email.code.confirmation")
public class EmailCodeConfirmationProperties {

    private String senderEmail;

    private String subjectText;
}
