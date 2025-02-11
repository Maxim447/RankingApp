package ru.hse.rankingapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурационный класс для работы с почтой.
 */
@Data
@Component
@ConfigurationProperties(value = "email.links")
public class EmailLinkProperties {

    private String addToOrganization;

    private String recoveryPassword;
}
