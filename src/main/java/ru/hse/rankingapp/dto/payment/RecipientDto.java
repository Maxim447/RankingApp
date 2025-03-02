package ru.hse.rankingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто с получателем.
 */
@Data
@Accessors(chain = true)
public class RecipientDto {

    @JsonProperty(value = "account_id")
    private String accountId;

    @JsonProperty(value = "gateway_id")
    private String gatewayId;
}
