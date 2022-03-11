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
public class OrganisationTransactionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("organisation_id")
    private Long user_id;

    @JsonProperty("organisation_name")
    private String organisation_name;

    @JsonProperty("transaction_date")
    private LocalDate transactionDate;

    @JsonProperty("crypto_symbol")
    private String cryptoSymbol;

    @JsonProperty("amount")
    private BigDecimal cryptoAmount;

    @JsonProperty("money")
    private BigDecimal money;

    @JsonProperty("worth_now")
    private BigDecimal worthNow;
}
