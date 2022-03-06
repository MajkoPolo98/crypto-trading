package com.cryptoGame.externalApis.cryptoStock.nomics;

import com.cryptoGame.domain.Coin;
import com.cryptoGame.domain.dtos.CoinDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NomicsClientTest {

    @Autowired
    private NomicsClient client;

    @Test
    void getCoins() {
        List<CoinDto> coinList = client.getCoins("ETH", "ETH");
        BigDecimal price = coinList.get(0).getPrice();
        BigDecimal amountPLN = new BigDecimal("1000");
        BigDecimal result = amountPLN.divide(price, 8, RoundingMode.HALF_DOWN).setScale(8);
        System.out.println(price);
        System.out.println(amountPLN);
        System.out.println(result);

    }

    @Test
    void getPrice() {

    }
}