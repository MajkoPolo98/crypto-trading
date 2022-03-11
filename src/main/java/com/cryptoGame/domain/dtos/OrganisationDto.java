package com.cryptoGame.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("organisation_name")
    private String groupName;

    @JsonProperty("organisation_funds")
    private BigDecimal groupFunds;

    @JsonProperty("organisation_wallet")
    private Map<String, BigDecimal> crypto;

    @JsonProperty("users")
    private List<Long> users;

}
