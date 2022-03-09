package com.cryptoGame.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @JsonProperty("country_name")
    private String country_name;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("city")
    private String city;

    @JsonProperty("ip")
    private String ip;
}
