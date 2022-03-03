package com.cryptoGame.domain.dtos;

import com.cryptoGame.domain.Coin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinListDto {

    @JsonProperty("coin_map")
    private Map<String, Coin> coinMap;
}
