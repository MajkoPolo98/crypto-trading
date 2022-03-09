package com.cryptoGame.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UserTransactionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id")
    private Long user_id;

    @JsonProperty("transaction_date")
    private LocalDate transactionDate;

    @JsonProperty("crypto_symbol")
    private String cryptoSymbol;

    @JsonProperty("amount")
    private String cryptoAmount;

    @JsonProperty("money")
    private String money;

    @JsonProperty("worth_now")
    private String worthNow;

}
