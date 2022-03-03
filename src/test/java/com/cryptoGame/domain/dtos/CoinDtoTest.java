package com.cryptoGame.domain.dtos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CoinDtoTest {

    @Test
    void getSymbol() {
        CoinDto dto = new CoinDto("BTC", "Bitcoin", new BigDecimal("60000"), "google.com");

        String symbol = dto.getSymbol();

        assertEquals("BTC", symbol);
    }
}