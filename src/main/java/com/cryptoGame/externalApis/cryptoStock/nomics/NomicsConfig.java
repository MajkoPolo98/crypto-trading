package com.cryptoGame.externalApis.cryptoStock.nomics;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class NomicsConfig {

    @Value("${nomics.api.endpoint.prod}")
    private String nomicsApi;

    @Value("${nomics.api.key}")
    private String nomicsKey;

    @Value("${nomics.default.currencies}")
    private String nomicsMainCurrencies;

}
