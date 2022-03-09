package com.cryptoGame.externalApis.cryptoStock.nomics;

import com.cryptoGame.domain.dtos.CoinDto;
import com.cryptoGame.externalApis.cryptoStock.CryptoStockClient;

import com.cryptoGame.externalApis.locationApi.IpApiClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NomicsClient implements CryptoStockClient {

    private final RestTemplate restTemplate;
    private final NomicsConfig config;
    private final IpApiClient ipApi;

    private static final Logger LOGGER = LoggerFactory.getLogger(NomicsClient.class);

    @Override
    public List<CoinDto> getCoins(String... coinSymbols) {
        URI url = UriComponentsBuilder.fromHttpUrl(
                        config.getNomicsApi() + "/currencies/ticker?per-page=100&page=1")
                .queryParam("convert", ipApi.getUserLocation().getCurrency())
                .queryParam("key", config.getNomicsKey())
                .queryParam("ids", String.join(",", coinSymbols))
                .build().encode().toUri();

        try {
            CoinDto[] response = restTemplate.getForObject(url, CoinDto[].class);
            return Optional.ofNullable(response)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
