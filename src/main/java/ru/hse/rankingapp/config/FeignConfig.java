package ru.hse.rankingapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг фейна.
 */
@Configuration
@EnableFeignClients(basePackages = "ru.hse.rankingapp")
public class FeignConfig {

    @Value("${payment.id}")
    private String accountId;

    @Value("${payment.key}")
    private String secretKey;


    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public BasicAuthRequestInterceptor authInterceptor() {
        return new BasicAuthRequestInterceptor(accountId, secretKey);
    }
}
