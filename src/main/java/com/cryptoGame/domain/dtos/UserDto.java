package com.cryptoGame.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("admin")
    private boolean adminStatus;

    @JsonProperty("money_PLN")
    private String money;

    @JsonProperty("group_name")
    private String group_name;

    @JsonProperty("wallet")
    private Map<String, BigDecimal> crypto;

    public UserDto(String userName, String email, String password, boolean adminStatus, String money) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.adminStatus = adminStatus;
        this.money = money;
    }
}
