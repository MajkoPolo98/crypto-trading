package com.cryptoGame.externalApis.nbpApi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class nbpApiClient {

    private final RestTemplate restTemplate;

    public BigDecimal getCurrencyRateInPLN;
}
