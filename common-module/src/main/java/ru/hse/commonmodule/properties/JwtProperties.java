package ru.hse.commonmodule.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурационный класс для jwt токена.
 */
@Data
@ConfigurationProperties("security.jwt")
@Component
public class JwtProperties {

    private String secret;

    private Long expiresIn;
}
