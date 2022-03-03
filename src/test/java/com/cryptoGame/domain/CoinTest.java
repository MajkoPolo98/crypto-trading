package com.cryptoGame.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class CoinTest {

    @Test
    void getName() {
        Coin coin = new Coin("BTC", "Bitcoin", new BigDecimal("60000"));

        String symbol = coin.getSymbol();

        assertEquals("BTC", symbol);
    }
}